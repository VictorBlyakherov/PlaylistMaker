package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.domainModule
import com.example.playlistmaker.di.viewModelModule
import com.example.playlistmaker.domain.settings.SettingInteractor
import com.example.playlistmaker.domain.settings.impl.SettingInteractorImpl
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

const val PLAYLIST_SETTINGS = "playlist_settings"
const val DARK_THEME_KEY = "darkTheme"

class App : Application() {

    var darkTheme = false
    private lateinit var sharedPrefs: SharedPreferences
    private val settingInteractor: SettingInteractor by inject()

    override fun onCreate() {
        super.onCreate()


        startKoin {
            androidContext(this@App)

            modules(dataModule, domainModule, viewModelModule)
        }


        darkTheme = settingInteractor.getTheme()

        switchTheme(darkTheme)
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