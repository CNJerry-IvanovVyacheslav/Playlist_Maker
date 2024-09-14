package com.melongame.playlistmaker.settings.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.melongame.playlistmaker.databinding.FragmentSettingsBinding
import com.melongame.playlistmaker.settings.domain.model.ThemeSettings
import com.melongame.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<SettingsViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.themeSwitcher.isChecked = viewModel.getThemeSettings().isNightMode

        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.updateThemeSetting(ThemeSettings(checked), checked)
            viewModel.switchTheme(checked)
        }

        binding.shareTheApp.setOnClickListener {
            viewModel.shareApp()
        }

        binding.writeInSupport.setOnClickListener {
            viewModel.openSupport()
        }

        binding.agreement.setOnClickListener {
            viewModel.openTerms()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}