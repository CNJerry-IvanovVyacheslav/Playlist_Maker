package com.melongame.playlistmaker.search.data.dto

import com.google.gson.annotations.SerializedName


data class TracksResponse(@SerializedName("results") val tracks: ArrayList<TrackDto>) : Response()