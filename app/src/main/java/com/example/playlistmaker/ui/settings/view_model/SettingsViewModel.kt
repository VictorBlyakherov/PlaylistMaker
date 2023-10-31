package com.example.playlistmaker.ui.settings.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.example.playlistmaker.domain.settings.SettingInteractor
import com.example.playlistmaker.domain.sharing.SharingInteractor


class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingInteractor: SettingInteractor,
) : ViewModel() {
    private var _currentThemeMutable = MutableLiveData<Boolean>()
    val currentTheme: LiveData<Boolean> = _currentThemeMutable


    init {
        getTheme()
    }


    fun shareLink() {
        sharingInteractor.shareApp()
    }

    fun openLink() {
        sharingInteractor.openTerms()
    }

    fun sendSupport() {
        sharingInteractor.openSupport()
    }

    fun getTheme() {
        _currentThemeMutable.value = settingInteractor.getTheme()
    }

    fun setTheme(theme: Boolean) {
        settingInteractor.setTheme(theme)
        _currentThemeMutable.value = theme
    }

}