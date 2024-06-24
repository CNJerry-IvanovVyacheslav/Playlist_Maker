package com.melongame.playlistmaker.ui.player

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.melongame.playlistmaker.R
import com.melongame.playlistmaker.additional_fun.dpToPx
import com.melongame.playlistmaker.domain.api.MediaPlayerInteractor
import com.melongame.playlistmaker.domain.impl.MediaPlayerInteractorImpl
import com.melongame.playlistmaker.domain.models.Track
import com.melongame.playlistmaker.additional_fun.timeFormat

class PlayerActivity : AppCompatActivity() {
    private lateinit var mediaPlayerInteractor: MediaPlayerInteractor
    private lateinit var playerPlayTrack: ImageButton
    private lateinit var playerTimePlnw: TextView
    private lateinit var handler: Handler
    private var updateSeekBar = Runnable {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val backButton = findViewById<ImageButton>(R.id.player_back_button)

        backButton.setOnClickListener {
            finish()
        }

        val track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("track", Track::class.java)
        } else {
            intent.getParcelableExtra<Track>("track")
        }

        if (track != null) {
            val trackName = track.trackName
            val artistName = track.artistName
            val trackTimeMillis = track.trackTimeMillis
            val collectionName = track.collectionName
            val releaseDate = track.releaseDate
            val primaryGenreName = track.primaryGenreName
            val country = track.country
            val artworkUrl100 = track.artworkUrl100
            val previewUrl = track.previewUrl

            val playerAlbum = findViewById<TextView>(R.id.player_album)
            val playerTrackName = findViewById<TextView>(R.id.player_track_name)
            val playerArtistName = findViewById<TextView>(R.id.player_artist_name)
            val playerTrackTime = findViewById<TextView>(R.id.player_track_time_mills)
            val playerCollectionName = findViewById<TextView>(R.id.player_album_name)
            val playerReleaseDate = findViewById<TextView>(R.id.release_date)
            val playerPrimaryGenre = findViewById<TextView>(R.id.player_primary_genre)
            val playerCountry = findViewById<TextView>(R.id.player_country_name)
            val playerArtworkImage = findViewById<ImageView>(R.id.player_image)

            mediaPlayerInteractor = MediaPlayerInteractorImpl()
            mediaPlayerInteractor.setDataSource(previewUrl)

            playerPlayTrack = findViewById(R.id.player_play_track)
            playerTimePlnw = findViewById(R.id.player_time)
            handler = Handler(Looper.getMainLooper())

            playerPlayTrack.setOnClickListener {
                if (mediaPlayerInteractor.isPlaying()) {
                    mediaPlayerInteractor.pause()
                } else {
                    mediaPlayerInteractor.start()
                    updateSeekBar()
                }
                updatePlayPauseButton()
            }

            mediaPlayerInteractor.setOnCompletionListener {
                playerTimePlnw.text = timeFormat.format(0)
                mediaPlayerInteractor.seekTo(0)
                updatePlayPauseButton()
            }

            playerTrackName.text = trackName
            playerArtistName.text = artistName
            playerTrackTime.text = timeFormat.format(trackTimeMillis.toLong())

            if (collectionName != "") {
                playerCollectionName.text = collectionName
            } else {
                playerAlbum.isVisible = false
                playerCollectionName.isVisible = false
            }

            playerReleaseDate.text = releaseDate.substring(0, 4)
            playerPrimaryGenre.text = primaryGenreName
            playerCountry.text = country

            Glide.with(this)
                .load(artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
                .centerCrop()
                .transform(RoundedCorners(dpToPx(8f, this)))
                .placeholder(R.drawable.placeholder_big)
                .into(playerArtworkImage)
        }
    }

    private fun updatePlayPauseButton() {
        val isPlaying = mediaPlayerInteractor.isPlaying()
        val iconResource = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play_track
        playerPlayTrack.setImageResource(iconResource)
    }

    private fun updateSeekBar() {
        updateSeekBar = Runnable {
            val currentTime = mediaPlayerInteractor.getCurrentPosition()
            playerTimePlnw.text = timeFormat.format(currentTime.toLong())
            handler.postDelayed(updateSeekBar, 1000)
        }
        handler.postDelayed(updateSeekBar, 0)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(updateSeekBar)
        mediaPlayerInteractor.pause()
        updatePlayPauseButton()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayerInteractor.release()
    }
}