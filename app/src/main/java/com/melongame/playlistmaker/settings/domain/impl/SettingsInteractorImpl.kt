package com.melongame.playlistmaker.settings.domain.impl

import com.melongame.playlistmaker.settings.data.SettingsRepository
import com.melongame.playlistmaker.settings.domain.SettingsInteractor
import com.melongame.playlistmaker.settings.domain.model.ThemeSettings

class SettingsInteractorImpl(
    private val settingsRepository: SettingsRepository,
) : SettingsInteractor {

    override fun getThemeSettings(): ThemeSettings {
        return settingsRepository.getThemeSettings()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        settingsRepository.updateThemeSetting(settings)
    }
}