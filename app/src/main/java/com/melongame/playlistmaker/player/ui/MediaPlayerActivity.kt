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
import com.melongame.playlistmaker.search.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class MediaPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private lateinit var playButton: ImageView
    private lateinit var updateProgressRunnable: Runnable
    private lateinit var playerTime: TextView
    private lateinit var viewModel: MediaPlayerViewModel
    private val handler = Handler(Looper.getMainLooper())
    private val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("Player", "Плейер создан")

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val trackJsonString = intent.getStringExtra(KEY_TRACK_JSON)

        viewModel = ViewModelProvider(
            this,
            MediaPlayerViewModel.getViewModelFactory()
        )[MediaPlayerViewModel::class.java]
        Log.i("Player", "Взята viewModel из провайдера")

        viewModel.setTrack(trackJsonString!!)
        Log.i("Player", "Трек установлен")

        val url = viewModel.pState.value?.track?.previewUrl
        Log.i("Player", "url превьюхи записан")

        val backButton = binding.playerBackButton
        playerTime = binding.playerTime

        playButton = binding.playerPlayTrack
        Log.i("Player", "вьюхи забинжены")

        viewModel.pState.observe(this) { state ->
            val track = state.track
            Log.i("Player", "обсервер сработал")

            showTrack(track!!)
            playButton.isEnabled = state.isPrepared
            playButton.setImageResource(if (state.isPlaying) R.drawable.ic_pause else R.drawable.ic_play_track)
            playerTime.text = dateFormat.format(state.currentPosition)
            Log.i("TimeChanged", "Время обновилось (по идее)!")
        }

        viewModel.preparePlayer(url)

        backButton.setOnClickListener { finish() }

        playButton.setOnClickListener { playbackControl() }


        updateProgressRunnable = object : Runnable {
            override fun run() {
                viewModel.updateCurrentPosition()
                Log.i("TimeChanged", "Время обновилось!")
                handler.postDelayed(this, TIME_DELAY)
            }
        }
        handler.post(updateProgressRunnable)
    }

    private fun showTrack(track: Track) {
        binding.apply {
            Log.i("Player", "Трек существует")
            playerTrackName.text = track.trackName
            playerArtistName.text = track.artistName
            releaseDate.text = track.releaseDate.substring(0, 4)
            playerPrimaryGenre.text = track.primaryGenreName
            playerCountryName.text = track.country
            playerTrackTimeMills.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime)

            Glide.with(playerImage)
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
                .into(playerImage)

            val isCollectionNameVisible = viewModel.getCollectionNameVisibility(track)
            playerAlbumName.isVisible = isCollectionNameVisible
            playerAlbum.isVisible = isCollectionNameVisible

            if (isCollectionNameVisible) {
                playerAlbumName.text = track.collectionName
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
        Log.i("Player", "Пауза")
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.release()
        handler.removeCallbacks(updateProgressRunnable)
        Log.i("Player", "Активити плейера уничтожена")
    }

    private fun playbackControl() {
        viewModel.playbackControl()
    }

    private companion object {
        const val KEY_TRACK_JSON = "trackJson"
        const val TIME_DELAY = 500L
    }
}