package com.melongame.playlistmaker.player.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.melongame.playlistmaker.R
import com.melongame.playlistmaker.databinding.ActivityPlayerBinding
import com.melongame.playlistmaker.media.ui.activity.CreatePlaylistFragment
import com.melongame.playlistmaker.media.ui.activity.PlaylistTrackAdapter
import com.melongame.playlistmaker.player.domain.models.AudioPlayerState
import com.melongame.playlistmaker.player.domain.models.TrackAddStatus
import com.melongame.playlistmaker.player.ui.view_model.MediaPlayerViewModel
import com.melongame.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale


class MediaPlayerActivity : AppCompatActivity() {

    private val viewModel: MediaPlayerViewModel by viewModel()
    private lateinit var binding: ActivityPlayerBinding
    private val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var adapter: PlaylistTrackAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("Player", "Плейер создан")

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = PlaylistTrackAdapter()
        binding.playlistRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.playlistRecyclerView.adapter = adapter

        val track = intent.getParcelableExtra<Track>(KEY_TRACK)

        val url = track?.previewUrl

        if (track != null) {

            viewModel.isFavorite.observe(this) { isFavorite ->
                updateFavoriteButton(isFavorite)
            }
            viewModel.setIsFavorite(track.isFavorite)
        }

        adapter.onItemClick = { playlist ->
            if (track != null) {
                viewModel.addTrackToPlaylist(track, playlist)
            }
        }

        viewModel.playerState.observe(this) { state ->

            if (track != null) {
                binding.playerTrackName.text = track.trackName
                binding.playerArtistName.text = track.artistName
                binding.releaseDate.text = track.releaseDate.substring(0, 4)
                binding.playerPrimaryGenre.text = track.primaryGenreName
                binding.playerCountryName.text = track.country
                binding.playerTrackTimeMills.text = dateFormat.format(track.trackTime)
                binding.playerTime.text = dateFormat.format(state.progress)
                binding.playerPlayTrack.isEnabled = state.isPlayButtonEnabled


                Glide.with(this).load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
                    .placeholder(R.drawable.placeholder).centerCrop().transform(
                        RoundedCorners(
                            TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP, 8F, resources.displayMetrics
                            ).toInt()
                        )
                    ).into(binding.playerImage)

                val isCollectionNameVisible = viewModel.getCollectionNameVisibility(track)
                binding.playerAlbumName.isVisible = isCollectionNameVisible
                binding.playerAlbum.isVisible = isCollectionNameVisible

                if (isCollectionNameVisible) {
                    binding.playerAlbumName.text = track.collectionName
                }

                updatePlayButtonState()
            }
        }
        viewModel.trackAddStatus.observe(this) { status ->
            when (status) {
                is TrackAddStatus.TrackAlreadyAdded -> showCustomToast(
                    getString(R.string.already_in_playlist, status.playlistName)
                )

                is TrackAddStatus.TrackAdded -> {
                    showCustomToast(getString(R.string.added_to_playlist, status.playlistName))
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                }

                is TrackAddStatus.TrackAddError -> showCustomToast(
                    getString(R.string.failed_to_add_to_playlist, status.playlistName)
                )
            }
        }

        viewModel.preparePlayer(url)

        binding.playerBackButton.setOnClickListener { finish() }

        binding.playerPlayTrack.setOnClickListener { playbackControl() }

        binding.playerLikeTrack.setOnClickListener {
            if (track != null) {
                viewModel.onFavoriteClicked(track)
            }
        }

        binding.playerAddTrack.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        viewModel.loadPlaylists()
        viewModel.playlists.observe(this) { playlists ->
            adapter.updatePlaylists(playlists)
        }

        binding.newPlaylistButton.setOnClickListener {
            navigateToCreatePlaylist()
        }

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        bottomSheetBehavior.apply {
            state = BottomSheetBehavior.STATE_HIDDEN

            bottomSheetBehavior.addBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> binding.overlay.visibility = View.GONE
                        else -> binding.overlay.visibility = View.VISIBLE
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                }
            })
        }

    }

    override fun onResume() {
        super.onResume()
        val track = intent.getParcelableExtra<Track>(KEY_TRACK)
        if (track != null) {
            lifecycleScope.launch {
                val isFavorite = viewModel.isTrackFavorite(track.trackId)
                viewModel.setIsFavorite(isFavorite)
                track.isFavorite = isFavorite
            }
        }

        updatePlayButtonState()
    }

    private fun updatePlayButtonState() {
        val currentState = viewModel.playerState.value
        when (currentState) {
            is AudioPlayerState.Playing -> binding.playerPlayTrack.setImageResource(R.drawable.ic_pause)
            else -> binding.playerPlayTrack.setImageResource(R.drawable.ic_play_track)
        }
    }

    private fun navigateToCreatePlaylist() {
        supportFragmentManager.commit {
            replace(
                R.id.fragmentContainer, CreatePlaylistFragment.newInstance()
            )
            addToBackStack(null)
        }

    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
        updatePlayButtonState()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.release()
    }

    private fun playbackControl() {
        viewModel.playbackControl()
    }

    private fun updateFavoriteButton(isFavorite: Boolean) {
        if (isFavorite) {
            binding.playerLikeTrack.setImageResource(R.drawable.ic_like_track_red)
        } else {
            binding.playerLikeTrack.setImageResource(R.drawable.ic_like_track)
        }
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CREATE_PLAYLIST && resultCode == Activity.RESULT_OK) {
            viewModel.loadPlaylists()
        }
    }

    private fun showCustomToast(message: String) {
        val inflater = layoutInflater
        val layout: View = inflater.inflate(R.layout.custom_toast, null)

        val text: TextView = layout.findViewById(R.id.toast_text)
        text.text = message

        val toast = Toast(this)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        toast.setGravity(Gravity.BOTTOM or Gravity.FILL_HORIZONTAL, 0, 0)
        toast.show()
    }

    companion object {
        private const val KEY_TRACK = "track"
        private const val REQUEST_CODE_CREATE_PLAYLIST = 100
    }
}