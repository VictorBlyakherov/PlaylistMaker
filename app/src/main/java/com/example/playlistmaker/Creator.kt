package com.example.playlistmaker

import com.example.playlistmaker.domain.api.PlayTrackInteractor
import com.example.playlistmaker.domain.impl.PlayTrackInteractorImpl
import com.example.playlistmaker.domain.impl.PlayTrackRepositoryImpl

object Creator {
    fun providePlayTrackInteractor(): PlayTrackInteractor {
        return PlayTrackInteractorImpl(PlayTrackRepositoryImpl())
    }
}