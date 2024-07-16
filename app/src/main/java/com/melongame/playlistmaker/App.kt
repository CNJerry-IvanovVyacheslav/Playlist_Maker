package com.melongame.playlistmaker

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    private var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        loadTheme()
        Log.d("ThemeSwitcher", "Тема загружена!")
        applyTheme()
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        saveTheme()
        applyTheme()
        Log.d("ThemeSwitcher", "Тема изменилась!")
    }

    private fun applyTheme() {
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    private fun saveTheme() {
        val prefs: SharedPreferences = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putBoolean(DARK_THEME_KEY, darkTheme)
        editor.apply()
    }

    private fun loadTheme() {
        val prefs: SharedPreferences = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        darkTheme = prefs.getBoolean(DARK_THEME_KEY, false)
    }

    private companion object {
        const val PREFERENCES_NAME = "PrefName"
        const val DARK_THEME_KEY = "DARK_THEME_KEY"
    }
}