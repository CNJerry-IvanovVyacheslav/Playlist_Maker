package com.melongame.playlistmaker.settings.data.impl

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.melongame.playlistmaker.settings.data.SettingsRepository
import com.melongame.playlistmaker.settings.domain.model.ThemeSettings

class SettingsRepositoryImpl(private val preferences: SharedPreferences) : SettingsRepository {

    override fun getThemeSettings(): ThemeSettings {
        val isNightMode = preferences.getBoolean(DARK_THEME_KEY, false)
        return ThemeSettings(isNightMode)
    }

    override fun updateThemeSetting(settings: ThemeSettings, darkThemeEnabled: Boolean) {
        preferences.edit()
            .putBoolean(DARK_THEME_KEY, settings.isNightMode)
            .apply()
        switchTheme(settings.isNightMode)
    }

    override fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    companion object {
        const val DARK_THEME_KEY = "DARK_THEME_KEY"
    }

}