package com.melongame.playlistmaker.media.ui.activity

import android.util.TypedValue
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.melongame.playlistmaker.databinding.PlaylistGridLayoutBinding
import com.melongame.playlistmaker.media.domain.models.Playlist

class PlaylistViewHolder(private val binding: PlaylistGridLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(playlist: Playlist) {
        binding.playlistName.text = playlist.name
        binding.tvPlaylistTrackQty.text = PlaylistTrackViewHolder.trackCount(playlist.trackCount)

        if (playlist.coverImagePath.isNullOrEmpty()) {
            binding.playlistCover.isVisible = false
            binding.placeholderImage.isVisible = true
        } else {
            binding.placeholderImage.isVisible = false
            binding.playlistCover.isVisible = true
            Glide.with(itemView.context)
                .load(playlist.coverImagePath)
                .transform(
                    CenterCrop(), RoundedCorners(
                        TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            8F,
                            itemView.context.resources.displayMetrics
                        ).toInt()
                    )
                )
                .into(binding.playlistCover)
        }
    }
}