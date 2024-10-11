package com.melongame.playlistmaker.media.ui.activity

import android.util.TypedValue
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.melongame.playlistmaker.R
import com.melongame.playlistmaker.databinding.PlaylistLayoutBinding
import com.melongame.playlistmaker.media.domain.models.Playlist

class PlaylistTrackViewHolder(private val binding: PlaylistLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(playlist: Playlist) {
        binding.textViewPlaylistName.text = playlist.name
        binding.textViewTrackCount.text = trackCount(playlist.trackCount)

        Glide.with(itemView)
            .load(playlist.coverImagePath)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(
                CenterCrop(),
                RoundedCorners(
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        2F,
                        itemView.context.resources.displayMetrics
                    ).toInt()
                )
            )
            .into(binding.imageViewArtwork)
    }

    companion object {
        fun trackCount(trackQty: Int): String {
            if (trackQty % 100 in 5..20) return "$trackQty треков"
            if (trackQty % 10 == 1) return "$trackQty трек"
            return if (trackQty % 10 in 2..4) "$trackQty трека"
            else "$trackQty треков"
        }
    }
}