package com.example.playlistmaker.domain.api

import com.example.playlistmaker.PlayerState

interface PlayTrackRepository {

    fun prepare(url: String)
    fun start()
    fun pause()
    fun stop()
    fun setOnCompletionListener(listener: () -> Unit)
    fun getPlayerState() : PlayerState
}