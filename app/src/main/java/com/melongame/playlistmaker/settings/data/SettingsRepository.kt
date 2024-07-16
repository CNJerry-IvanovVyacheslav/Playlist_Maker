package com.melongame.playlistmaker.settings.data

import com.melongame.playlistmaker.settings.domain.model.ThemeSettings

interface SettingsRepository {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}