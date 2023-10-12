package com.example.playlistmaker.domain.settings

interface SettingInteractor {
    fun getTheme(): Boolean
    fun setTheme(theme: Boolean)
}