package com.melongame.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.melongame.playlistmaker.search.data.NetworkClient
import com.melongame.playlistmaker.search.data.dto.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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

    override suspend fun doRequest(dto: TrackSearchRequest): Response {

        if (!isConnected()) {
            return Response().apply { resultCode = NO_CONNECTION }
        }

        if (false) {
            return Response().apply { resultCode = BAD_REQUEST }
        }
        return withContext(Dispatchers.IO) {
            try {
                val resp = iTunesApi.getTracks(dto.expression)
                resp.apply { resultCode = RESULT_SUCCESS }

            } catch (e: Throwable) {
                Response().apply { resultCode = NO_CONNECTION }
            }
        }
    }

    companion object {
        private const val RESULT_SUCCESS = 200
        private const val BAD_REQUEST = 400
        private const val NO_CONNECTION = -1
    }
}