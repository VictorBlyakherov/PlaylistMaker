package com.example.playlistmaker.creator

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.domain.player.PlayTrackInteractor
import com.example.playlistmaker.domain.player.impl.PlayTrackInteractorImpl
import com.example.playlistmaker.data.player.impl.PlayTrackRepositoryImpl
import com.example.playlistmaker.data.search.impl.SearchHistoryImpl
import com.example.playlistmaker.data.search.impl.SearchRepositoryImpl
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.data.settings.impl.SettingRepositoryImpl
import com.example.playlistmaker.data.sharing.impl.ExternalNavigatorImpl
import com.example.playlistmaker.domain.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.SearchInteractor
import com.example.playlistmaker.domain.search.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.search.impl.SearchInteractorImpl
import com.example.playlistmaker.domain.settings.SettingInteractor
import com.example.playlistmaker.domain.settings.impl.SettingInteractorImpl
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.impl.SharingInteractorImpl

object Creator {
    fun providePlayTrackInteractor(): PlayTrackInteractor {
        return PlayTrackInteractorImpl(PlayTrackRepositoryImpl())
    }

    fun provideSearchHistoryInteractor(sharedPrefs: SharedPreferences): SearchHistoryInteractor {
        return  SearchHistoryInteractorImpl(SearchHistoryImpl(sharedPrefs))
    }

    fun provideSharingInteractor(context: Context): SharingInteractor {
        return  SharingInteractorImpl(ExternalNavigatorImpl(context))
    }

    fun provideSettingInteractor(sharedPrefs: SharedPreferences): SettingInteractor {
        return  SettingInteractorImpl(SettingRepositoryImpl(sharedPrefs))
    }

    fun provideSearchInteractor(): SearchInteractor {
        return  SearchInteractorImpl(SearchRepositoryImpl(RetrofitNetworkClient()))
    }




}