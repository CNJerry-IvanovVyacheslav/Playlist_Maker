package com.melongame.playlistmaker.settings.data.impl

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.melongame.playlistmaker.App
import com.melongame.playlistmaker.settings.data.SettingsRepository
import com.melongame.playlistmaker.settings.domain.model.ThemeSettings

class SettingsRepositoryImpl(application: Application) : SettingsRepository {
    private val preferences: SharedPreferences =
        application.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    private val app = application as App

    override fun getThemeSettings(): ThemeSettings {
        val isNightMode = preferences.getBoolean(DARK_THEME_KEY, false)
        return ThemeSettings(isNightMode)
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        preferences.edit()
            .putBoolean(DARK_THEME_KEY, settings.isNightMode)
            .apply()
        app.switchTheme(settings.isNightMode)
    }

    companion object {
        const val PREFERENCES_NAME = "PrefName"
        const val DARK_THEME_KEY = "DARK_THEME_KEY"
    }

}