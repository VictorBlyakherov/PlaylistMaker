package com.example.playlistmaker

import android.content.SharedPreferences
import com.example.playlistmaker.domain.api.PlayTrackInteractor
import com.example.playlistmaker.domain.impl.PlayTrackInteractorImpl
import com.example.playlistmaker.data.impl.PlayTrackRepositoryImpl
import com.example.playlistmaker.data.impl.SearchHistoryImpl
import com.example.playlistmaker.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.impl.SearchHistoryInteractorImpl

object Creator {
    fun providePlayTrackInteractor(): PlayTrackInteractor {
        return PlayTrackInteractorImpl(PlayTrackRepositoryImpl())
    }

    fun provideSearchHistoryInteractor(sharedPrefs: SharedPreferences): SearchHistoryInteractor {
        return  SearchHistoryInteractorImpl(SearchHistoryImpl(sharedPrefs))
    }

}