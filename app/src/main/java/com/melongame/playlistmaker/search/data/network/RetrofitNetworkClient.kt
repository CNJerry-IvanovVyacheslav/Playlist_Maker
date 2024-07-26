package com.melongame.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.melongame.playlistmaker.search.data.NetworkClient
import com.melongame.playlistmaker.search.data.dto.Response

class RetrofitNetworkClient(
    private val iTunesApi: ITunesApi,
    private val context: Context,
) : NetworkClient {

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }

    override fun doRequest(dto: TrackSearchRequest): Response {

        if (isConnected()) {
            try {
                val resp = iTunesApi.getTracks(dto.expression).execute()

                val body = resp.body() ?: Response()

                return body.apply { resultCode = 200 }
            } catch (e: Exception) {
                return Response().apply { resultCode = 0 }
            }
        }
        return Response().apply { resultCode = -1 }
    }
}