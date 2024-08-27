package com.melongame.playlistmaker.search.ui.activity

import android.util.TypedValue
import androidx.recyclerview.widget.RecyclerView
import com.melongame.playlistmaker.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.melongame.playlistmaker.databinding.TrackListBinding
import com.melongame.playlistmaker.search.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TracksViewHolder(private val binding: TrackListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(track: Track) {
        binding.trackName.text = track.trackName
        binding.trackArtist.text = track.artistName
        binding.trackTime.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime)
        binding.trackArtist.requestLayout()

        Glide.with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(
                RoundedCorners(
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        2F,
                        itemView.context.resources.displayMetrics
                    ).toInt()
                )
            )
            .into(binding.trackImage)
    }
}