package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.creator.Creator

const val PLAYLIST_SETTINGS = "playlist_settings"
const val DARK_THEME_KEY = "darkTheme"

class App : Application() {

    var darkTheme = false
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        sharedPrefs = getSharedPreferences(PLAYLIST_SETTINGS, MODE_PRIVATE)

        val settingInteractor = Creator.provideSettingInteractor(sharedPrefs)


        darkTheme = settingInteractor.getTheme()

        switchTheme(darkTheme)
        settingInteractor.setTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}