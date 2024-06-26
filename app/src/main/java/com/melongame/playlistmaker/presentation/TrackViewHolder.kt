package com.melongame.playlistmaker.presentation

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.melongame.playlistmaker.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.melongame.playlistmaker.domain.models.Track
import com.melongame.playlistmaker.additional_fun.timeFormat

class TrackViewHolder(view: View, private val imageCornersDp: Int) : RecyclerView.ViewHolder(view) {

    private val name: TextView = view.findViewById(R.id.track_name)
    private val artist: TextView = view.findViewById(R.id.track_artist)
    private val time: TextView = view.findViewById(R.id.track_time)
    private val image: ImageView = view.findViewById(R.id.track_image)

    fun bind(track: Track) {
        name.text = track.trackName
        artist.text = track.artistName
        time.text = timeFormat.format(track.trackTimeMillis.toLong())

        Glide.with(itemView.context)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .fitCenter()
            .transform(RoundedCorners(imageCornersDp))
            .into(image)
    }
}