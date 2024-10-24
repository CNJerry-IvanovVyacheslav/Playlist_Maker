package com.melongame.playlistmaker.search.ui.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.melongame.playlistmaker.databinding.TrackListBinding
import com.melongame.playlistmaker.search.domain.models.Track

class TrackAdapter : RecyclerView.Adapter<TracksViewHolder>() {

    var tracks: List<Track> = ArrayList()
    var onItemClick: ((Track) -> Unit)? = null
    var onItemLongClick: ((Track) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        val viewHolder = LayoutInflater.from(parent.context)
        return TracksViewHolder(TrackListBinding.inflate(viewHolder, parent, false))
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(tracks[position])
        }

        holder.itemView.setOnLongClickListener {
            onItemLongClick?.invoke(tracks[position])
            true
        }
    }

    override fun getItemCount(): Int = tracks.size
    fun updateTracks(tracks: List<Track>) {
        this.tracks = tracks
        notifyDataSetChanged()
    }
}