package com.example.playlistmaker.data.settings.impl

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.DARK_THEME_KEY
import com.example.playlistmaker.data.settings.SettingRepository

class SettingRepositoryImpl(private val sharedPreferences: SharedPreferences) : SettingRepository {
    override fun getStoredTheme(): Boolean {
        return sharedPreferences.getBoolean(DARK_THEME_KEY, false)

    }

    override fun setTheme(theme: Boolean) {
        sharedPreferences.edit()
            .putBoolean(DARK_THEME_KEY, theme)
            .apply()
    }
}