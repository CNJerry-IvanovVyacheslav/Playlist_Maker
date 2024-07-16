package com.melongame.playlistmaker.settings.ui.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.melongame.playlistmaker.settings.domain.SettingsInteractor
import com.melongame.playlistmaker.settings.domain.model.ThemeSettings
import com.melongame.playlistmaker.sharing.domain.SharingInteractor
import com.melongame.playlistmaker.util.Creator

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingInteractor: SettingsInteractor,
) : ViewModel() {


    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun openTerms() {
        sharingInteractor.openTerms()
    }

    fun openSupport() {
        sharingInteractor.openSupport()
    }

    fun getThemeSettings(): ThemeSettings {
        return settingInteractor.getThemeSettings()
    }

    fun updateThemeSetting(settings: ThemeSettings) {
        settingInteractor.updateThemeSetting(settings)
    }


    companion object {
        fun getViewModelFactory(application: Application): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val sharingInteractor = Creator.provideSharingInteractor(application)
                    val settingInteractor = Creator.provideSettingsInteractor(application)
                    SettingsViewModel(sharingInteractor, settingInteractor)
                }
            }
    }
}