package com.example.playlistmaker.di

import com.example.playlistmaker.ui.media.view_model.FavoritesFragmentViewModel
import com.example.playlistmaker.ui.media.view_model.PlaylistAddFragmentViewModel
import com.example.playlistmaker.ui.media.view_model.PlaylistFragmentViewModel
import com.example.playlistmaker.ui.player.view_model.TrackDetailViewModel
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel<TrackDetailViewModel> {
        TrackDetailViewModel(get(), get(), get())
    }

    viewModel<SearchViewModel> {
        SearchViewModel(get(), get(), get())
    }

    viewModel<SettingsViewModel> {
        SettingsViewModel(get(), get())
    }

    viewModel<FavoritesFragmentViewModel> {
        FavoritesFragmentViewModel(get())
    }

    viewModel<PlaylistFragmentViewModel> {
        PlaylistFragmentViewModel(get())
    }

    viewModel<PlaylistAddFragmentViewModel> {
        PlaylistAddFragmentViewModel(get())
    }


}