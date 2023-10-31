package com.example.playlistmaker.di

import com.example.playlistmaker.data.player.PlayTrackRepository
import com.example.playlistmaker.data.player.impl.PlayTrackRepositoryImpl
import com.example.playlistmaker.data.search.AppleMusicApi
import com.example.playlistmaker.data.search.SearchHistoryRepository
import com.example.playlistmaker.data.search.SearchRepository
import com.example.playlistmaker.data.search.impl.SearchHistoryImpl
import com.example.playlistmaker.data.search.impl.SearchRepositoryImpl
import com.example.playlistmaker.data.search.network.NetworkClient
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.data.settings.SettingRepository
import com.example.playlistmaker.data.settings.impl.SettingRepositoryImpl
import com.example.playlistmaker.data.sharing.ExternalNavigator
import com.example.playlistmaker.data.sharing.impl.ExternalNavigatorImpl
import com.google.gson.Gson
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val dataModule = module {
    val appleBaseUrl = "https://itunes.apple.com"

    single<AppleMusicApi> {
            Retrofit.Builder().baseUrl(appleBaseUrl).addConverterFactory(GsonConverterFactory.create())
                .build().create(AppleMusicApi::class.java)
    }

    factory { Gson() }

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }


    factory<PlayTrackRepository> {
        PlayTrackRepositoryImpl()
    }

    factory<SearchHistoryRepository> {
        SearchHistoryImpl(context = get())
    }

    factory<SettingRepository> {
        SettingRepositoryImpl(context = get())
    }

    factory<SearchRepository> {
        SearchRepositoryImpl(get())
    }

}