package com.melongame.playlistmaker.settings.domain

import com.melongame.playlistmaker.settings.domain.model.ThemeSettings

interface SettingsInteractor {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings, darkThemeEnabled: Boolean)
    fun switchTheme(darkThemeEnabled: Boolean)
}