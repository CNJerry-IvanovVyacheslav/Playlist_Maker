package com.melongame.playlistmaker.settings.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import com.google.android.material.switchmaterial.SwitchMaterial
import com.melongame.playlistmaker.R
import com.melongame.playlistmaker.settings.domain.model.ThemeSettings
import com.melongame.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val back = findViewById<ImageView>(R.id.back_light)
        val share = findViewById<FrameLayout>(R.id.shareTheApp)
        val support = findViewById<FrameLayout>(R.id.writeInSupport)
        val agreement = findViewById<FrameLayout>(R.id.agreement)
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)

        themeSwitcher.isChecked = viewModel.getThemeSettings().isNightMode

        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.updateThemeSetting(ThemeSettings(checked), checked)
            viewModel.switchTheme(checked)
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