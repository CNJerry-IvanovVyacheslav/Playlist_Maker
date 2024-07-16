package com.melongame.playlistmaker.player.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.TypedValue
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.melongame.playlistmaker.databinding.ActivityPlayerBinding
import com.melongame.playlistmaker.R
import com.melongame.playlistmaker.player.ui.view_model.MediaPlayerViewModel
import java.text.SimpleDateFormat
import java.util.Locale

class MediaPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private lateinit var playButton: ImageView
    private lateinit var updateProgressRunnable: Runnable
    private lateinit var playerTime: TextView
    private lateinit var viewModel: MediaPlayerViewModel
    private val handler = Handler(Looper.getMainLooper())
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            MediaPlayerViewModel.getViewModelFactory()
        )[MediaPlayerViewModel::class.java]
        val trackJsonString = intent.getStringExtra(KEY_TRACK_JSON)

        viewModel.setTrack(trackJsonString)

        val url = viewModel.pState.value?.track?.previewUrl

        val playerTrackArtwork = binding.playerImage
        val playerTrackName = binding.playerTrackName
        val playerArtistName = binding.playerArtistName
        val playerCollectionName = binding.playerAlbumName
        val playerReleaseDate = binding.releaseDate
        val playerPrimaryGenreName = binding.playerPrimaryGenre
        val playerCountry = binding.playerCountryName
        val playerTrackTime = binding.playerTrackTimeMills
        val collectionNameTextView = binding.playerAlbum
        val backButton = binding.playerBackButton
        playerTime = binding.playerTime

        playButton = binding.playerPlayTrack

        viewModel.pState.observe(this) { state ->
            val track = state.track

            if (track != null) {
                playerTrackName.text = track.trackName
                playerArtistName.text = track.artistName
                playerReleaseDate.text = track.releaseDate.substring(0, 4)
                playerPrimaryGenreName.text = track.primaryGenreName
                playerCountry.text = track.country
                playerTrackTime.text =
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime)

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
                    .into(playerTrackArtwork)

                val isCollectionNameVisible = viewModel.getCollectionNameVisibility(track)
                playerCollectionName.isVisible = isCollectionNameVisible
                collectionNameTextView.isVisible = isCollectionNameVisible

                if (isCollectionNameVisible) {
                    playerCollectionName.text = track.collectionName
                }
            }
            playButton.isEnabled = state.isPrepared
            playButton.setImageResource(if (state.isPlaying) R.drawable.ic_pause else R.drawable.ic_play_track)
            playerTime.text = dateFormat.format(state.currentPosition)
            Log.d("TimeChanged", "Время обновилось (по идее)!")
        }

        viewModel.preparePlayer(url)

        backButton.setOnClickListener { finish() }

        playButton.setOnClickListener { playbackControl() }


        updateProgressRunnable = object : Runnable {
            override fun run() {
                viewModel.updateCurrentPosition()
                Log.d("TimeChanged", "Время обновилось!")
                handler.postDelayed(this, TIME_DELAY)
            }
        }
        handler.post(updateProgressRunnable)
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.release()
        handler.removeCallbacks(updateProgressRunnable)
    }

    private fun playbackControl() {
        viewModel.playbackControl()
    }

    companion object {
        private const val KEY_TRACK_JSON = "trackJson"
        private const val TIME_DELAY = 500L
    }
}