package com.example.playlistmaker.ui.settings.view_model

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.settings.SettingInteractor
import com.example.playlistmaker.domain.sharing.SharingInteractor


class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingInteractor: SettingInteractor,
) : ViewModel() {
    private var currentThemeMutable = MutableLiveData<Boolean>()
    val currentTheme: LiveData<Boolean> = currentThemeMutable


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
        currentThemeMutable.value = settingInteractor.getTheme()
    }

    fun setTheme(theme: Boolean) {
        settingInteractor.setTheme(theme)
        currentThemeMutable.value = theme
    }

    companion object {
        fun getViewModelFactory(sharedPreferences: SharedPreferences, context: Context): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SettingsViewModel(
                        Creator.provideSharingInteractor(context),
                        Creator.provideSettingInteractor(sharedPreferences)
                    ) as T
                }
            }
    }

}