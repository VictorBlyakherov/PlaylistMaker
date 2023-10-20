package com.example.playlistmaker.di

import com.example.playlistmaker.data.sharing.ExternalNavigator
import com.example.playlistmaker.data.sharing.impl.ExternalNavigatorImpl
import com.example.playlistmaker.domain.player.PlayTrackInteractor
import com.example.playlistmaker.domain.player.impl.PlayTrackInteractorImpl
import com.example.playlistmaker.domain.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.SearchInteractor
import com.example.playlistmaker.domain.search.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.search.impl.SearchInteractorImpl
import com.example.playlistmaker.domain.settings.SettingInteractor
import com.example.playlistmaker.domain.settings.impl.SettingInteractorImpl
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import org.koin.dsl.module


val domainModule = module {

    factory<SearchInteractor> {
        SearchInteractorImpl(get())
    }

    factory<SettingInteractor> {
        SettingInteractorImpl(get())
    }

    factory<SharingInteractor> {
        SharingInteractorImpl(get())
    }

    factory<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(get())
    }

    factory<PlayTrackInteractor> {
        PlayTrackInteractorImpl(get())
    }

    factory<ExternalNavigator> {
        ExternalNavigatorImpl(context = get())
    }

}