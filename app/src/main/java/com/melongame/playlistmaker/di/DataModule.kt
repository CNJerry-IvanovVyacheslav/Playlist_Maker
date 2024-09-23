package com.melongame.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import androidx.room.Room
import com.google.gson.Gson
import com.melongame.playlistmaker.media.data.db.AppDatabase
import com.melongame.playlistmaker.search.data.NetworkClient
import com.melongame.playlistmaker.search.data.network.ITunesApi
import com.melongame.playlistmaker.search.data.network.RetrofitNetworkClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single<ITunesApi> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApi::class.java)
    }
    single {
        androidContext()
            .getSharedPreferences(SEARCH_HISTORY_KEY, Context.MODE_PRIVATE)
    }
    factory { Gson() }
    single<NetworkClient> {
        RetrofitNetworkClient(get(), androidContext())
    }
    factory<MediaPlayer> {
        MediaPlayer()
    }
    single(named(NAME_OF_PREFERENCE)) {
        androidContext().getSharedPreferences(
            THEME_SWITCHER_KEY,
            Context.MODE_PRIVATE
        )
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, DATABASE_DB)
            .build()
    }

}

private const val BASE_URL = "https://itunes.apple.com"
private const val SEARCH_HISTORY_KEY = "search_history"
private const val NAME_OF_PREFERENCE = "Theme_prefs"
private const val THEME_SWITCHER_KEY = "theme_switcher"
private const val DATABASE_DB = "database.db"