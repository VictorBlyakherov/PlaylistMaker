package com.example.playlistmaker.ui.settings.activity


import android.content.SharedPreferences
import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider

import com.example.playlistmaker.PLAYLIST_SETTINGS

import com.example.playlistmaker.databinding.SettingsActivityBinding
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: SettingsActivityBinding
    private val settingsViewModel by viewModel<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = SettingsActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        settingsViewModel.currentTheme.observe(this) { currentTheme ->
            binding.themeSwitch.isChecked = currentTheme
        }

        binding.themeSwitch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            settingsViewModel.setTheme(isChecked)
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.MODE_NIGHT_NO
                }
            )
        })

        binding.back.setOnClickListener {
            this.finish()
        }

        binding.share.setOnClickListener {
            settingsViewModel.shareLink()
        }

        binding.shareView.setOnClickListener {
            settingsViewModel.shareLink()
        }

        binding.sendToSupport.setOnClickListener {
            settingsViewModel.sendSupport()
        }

        binding.supportView.setOnClickListener {
            settingsViewModel.sendSupport()
        }

        binding.userAgreementView.setOnClickListener {
            settingsViewModel.openLink()
        }

        binding.viewUserAgreement.setOnClickListener {
            settingsViewModel.openLink()
        }
    }

}
