package com.example.playlistmaker.domain.settings.impl

import com.example.playlistmaker.data.settings.SettingRepository
import com.example.playlistmaker.domain.settings.SettingInteractor

class SettingInteractorImpl(private  val settingRepository: SettingRepository): SettingInteractor {
    override fun getTheme(): Boolean {
        return settingRepository.getStoredTheme()
    }

    override fun setTheme(theme: Boolean) {
        settingRepository.setTheme(theme)
    }
}