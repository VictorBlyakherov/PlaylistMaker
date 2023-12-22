package com.example.playlistmaker.domain.player

import com.example.playlistmaker.domain.model.PlayerState

interface PlayTrackInteractor {
    fun preparePlayer(url: String)
    fun startPlayer()
    fun pausePlayer()
    fun stopPlayer()
    fun setTrackCompletionListener(listener: () -> Unit)
    fun getPlayerState(): PlayerState
    fun getCurrentPosition(): Int
}