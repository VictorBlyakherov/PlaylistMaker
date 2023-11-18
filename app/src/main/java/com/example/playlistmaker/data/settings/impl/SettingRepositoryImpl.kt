package com.example.playlistmaker.data.settings.impl

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.DARK_THEME_KEY
import com.example.playlistmaker.PLAYLIST_SETTINGS
import com.example.playlistmaker.data.settings.SettingRepository

class SettingRepositoryImpl(private val context: Context) : SettingRepository {
    private val sharedPreferences =
        context.getSharedPreferences(PLAYLIST_SETTINGS, AppCompatActivity.MODE_PRIVATE)

    override fun getStoredTheme(): Boolean {
        return sharedPreferences.getBoolean(DARK_THEME_KEY, false)

    }

    override fun setTheme(theme: Boolean) {
        sharedPreferences.edit()
            .putBoolean(DARK_THEME_KEY, theme)
            .apply()
    }
}