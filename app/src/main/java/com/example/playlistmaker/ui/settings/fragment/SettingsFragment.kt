package com.example.playlistmaker.ui.settings.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val settingsViewModel by viewModel<SettingsViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingsViewModel.currentTheme.observe(viewLifecycleOwner) { currentTheme ->
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