package com.melongame.playlistmaker

import android.app.Application
import android.util.Log
import com.melongame.playlistmaker.di.dataModule
import com.melongame.playlistmaker.di.interactorModule
import com.melongame.playlistmaker.di.repositoryModule
import com.melongame.playlistmaker.di.viewModelModule
import com.melongame.playlistmaker.settings.domain.SettingsInteractor
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {

    private var darkTheme = false
    private val settingsInteractor: SettingsInteractor by inject()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }

        darkTheme = settingsInteractor.getThemeSettings().isNightMode
        switchTheme(darkTheme)
    }

    private fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        settingsInteractor.switchTheme(darkThemeEnabled)
        Log.d("ThemeSwitcher", "Тема изменилась!")
    }
}