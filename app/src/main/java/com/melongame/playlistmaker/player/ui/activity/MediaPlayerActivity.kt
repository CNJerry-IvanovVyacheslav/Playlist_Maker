package com.melongame.playlistmaker.player.ui.activity

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.melongame.playlistmaker.databinding.ActivityPlayerBinding
import com.melongame.playlistmaker.R
import com.melongame.playlistmaker.player.domain.models.AudioPlayerState
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("Player", "Плейер создан")

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val track = intent.getParcelableExtra<Track>(KEY_TRACK)

        val url = track?.previewUrl

        if (track != null) {

            viewModel.isFavorite.observe(this) { isFavorite ->
                updateFavoriteButton(isFavorite)
            }
            viewModel.setIsFavorite(track.isFavorite)
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


                Glide.with(this)
                    .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
                    .placeholder(R.drawable.placeholder)
                    .centerCrop()
                    .transform(
                        RoundedCorners(
                            TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                8F,
                                resources.displayMetrics
                            ).toInt()
                        )
                    )
                    .into(binding.playerImage)

                val isCollectionNameVisible = viewModel.getCollectionNameVisibility(track)
                binding.playerAlbumName.isVisible = isCollectionNameVisible
                binding.playerAlbum.isVisible = isCollectionNameVisible

                if (isCollectionNameVisible) {
                    binding.playerAlbumName.text = track.collectionName
                }

                when (state) {
                    is AudioPlayerState.Playing -> binding.playerPlayTrack.setImageResource(R.drawable.ic_pause)

                    else -> binding.playerPlayTrack.setImageResource(R.drawable.ic_play_track)
                }
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

    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
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
            binding.playerLikeTrack.setImageResource(R.drawable.ic_like_track)
        } else {
            binding.playerLikeTrack.setImageResource(R.drawable.ic_like_track)
        }
    }

    companion object {
        private const val KEY_TRACK = "track"
    }
}