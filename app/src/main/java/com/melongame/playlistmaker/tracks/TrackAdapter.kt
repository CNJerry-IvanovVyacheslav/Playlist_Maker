package com.melongame.playlistmaker.tracks

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.melongame.playlistmaker.R
import com.melongame.playlistmaker.activity.PlayerActivity
import com.melongame.playlistmaker.activity.SearchHistoryControl
import com.melongame.playlistmaker.additional_fun.dpToPx

class TrackAdapter(
    private var context: Context,
    private var tracks: List<Track>,
    private val searchHistoryControl: SearchHistoryControl?
) : Adapter<TrackViewHolder>() {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val imageCornersPx = parent
            .getResources()
            .getDimension(R.dimen.dimens_2dp)

        val imageCornersDp = dpToPx(imageCornersPx, parent.context)

        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.track_list, parent, false)
        return TrackViewHolder(view, imageCornersDp)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)

        holder.itemView.setOnClickListener {
            if (clickDebounce()) {
                searchHistoryControl?.addToSearchHistory(track)
                navigateToAudioPlayer(track)
            }
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateTracks(newTracks: MutableList<Track>) {
        tracks = newTracks
        notifyDataSetChanged()
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun navigateToAudioPlayer(track: Track) {
        val intent = Intent(context, PlayerActivity::class.java)
        intent.putExtra("trackName", track.trackName)
        intent.putExtra("artistName", track.artistName)
        intent.putExtra("trackTimeMillis", track.trackTimeMillis)
        intent.putExtra("artworkUrl100", track.artworkUrl100)
        intent.putExtra("collectionName", track.collectionName)
        intent.putExtra("releaseDate", track.releaseDate)
        intent.putExtra("primaryGenreName", track.primaryGenreName)
        intent.putExtra("country", track.country)
        intent.putExtra("previewUrl", track.previewUrl)
        context.startActivity(intent)
    }
}