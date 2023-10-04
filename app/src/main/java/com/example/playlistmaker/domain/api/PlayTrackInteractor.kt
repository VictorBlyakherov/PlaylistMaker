package com.example.playlistmaker.domain.api

import com.example.playlistmaker.PlayerState
import java.util.EventListener

interface PlayTrackInteractor {
    fun preparePlayer(url: String)
    fun startPlayer()
    fun pausePlayer()
    fun stopPlayer()
    fun setTrackCompletionListener(listener: () -> Unit)
    fun getPlayerState() : PlayerState

}