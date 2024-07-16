package com.melongame.playlistmaker.settings.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.switchmaterial.SwitchMaterial
import com.melongame.playlistmaker.R
import com.melongame.playlistmaker.App
import com.melongame.playlistmaker.settings.domain.model.ThemeSettings
import com.melongame.playlistmaker.settings.ui.view_model.SettingsViewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        viewModel = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory(applicationContext as App)
        )[SettingsViewModel::class.java]

        val back = findViewById<ImageView>(R.id.back_light)
        val share = findViewById<FrameLayout>(R.id.shareTheApp)
        val support = findViewById<FrameLayout>(R.id.writeInSupport)
        val agreement = findViewById<FrameLayout>(R.id.agreement)
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)

        themeSwitcher.isChecked = viewModel.getThemeSettings().isNightMode

        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.updateThemeSetting(ThemeSettings(checked))
        }

        back.setOnClickListener { finish() }

        share.setOnClickListener {
            viewModel.shareApp()
        }

        support.setOnClickListener {
            viewModel.openSupport()
        }

        agreement.setOnClickListener {
            viewModel.openTerms()
        }
    }
}