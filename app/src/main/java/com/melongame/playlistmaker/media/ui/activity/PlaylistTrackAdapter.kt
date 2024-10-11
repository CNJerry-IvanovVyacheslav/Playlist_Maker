package com.melongame.playlistmaker.media.ui.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.melongame.playlistmaker.databinding.PlaylistLayoutBinding
import com.melongame.playlistmaker.media.domain.models.Playlist

class PlaylistTrackAdapter : RecyclerView.Adapter<PlaylistTrackViewHolder>() {

    var playlists: List<Playlist> = ArrayList()
    var onItemClick: ((Playlist) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistTrackViewHolder {
        val viewHolder = LayoutInflater.from(parent.context)
        return PlaylistTrackViewHolder(PlaylistLayoutBinding.inflate(viewHolder, parent, false))
    }

    override fun onBindViewHolder(holder: PlaylistTrackViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(playlists[position])
        }
    }

    override fun getItemCount(): Int = playlists.size
    fun updatePlaylists(newPlaylists: List<Playlist>) {
        playlists = newPlaylists
        notifyDataSetChanged()
    }
}