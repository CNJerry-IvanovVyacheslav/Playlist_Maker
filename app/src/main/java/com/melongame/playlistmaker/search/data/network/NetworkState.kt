package com.melongame.playlistmaker.search.data.network

enum class NetworkState(i: Int) {
    CONNECTION_SUCCESS(200),
    CONNECTION_FATAL_ERROR(1),
    CONNECTION_NO_INTERNET(-1),
    DEFAULT(0)
}