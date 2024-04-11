package com.melongame.playlistmaker.tracks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.melongame.playlistmaker.R
import com.melongame.playlistmaker.additional_fun.dpToPx

class TrackAdapter(
    private val tracks: List<Track>
) : Adapter<TrackViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val imageCornersPx = parent
            .getResources()
            .getDimension(R.dimen.dimens_2dp)

        val imageCornersDp = dpToPx(imageCornersPx, parent.context)

        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.track_list, parent,false)
        return TrackViewHolder(view, imageCornersDp)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}