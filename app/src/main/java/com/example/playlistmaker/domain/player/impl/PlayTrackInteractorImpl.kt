package com.example.playlistmaker.domain.player.impl

import com.example.playlistmaker.domain.model.PlayerState
import com.example.playlistmaker.data.player.PlayTrackRepository
import com.example.playlistmaker.domain.player.PlayTrackInteractor

class PlayTrackInteractorImpl(private val repository: PlayTrackRepository) : PlayTrackInteractor {

    override fun preparePlayer(url: String) {
        repository.prepare(url)
    }

    override fun startPlayer() {
        repository.start()
    }

    override fun pausePlayer() {
        repository.pause()
    }

    override fun stopPlayer() {
        repository.stop()
    }

    override fun setTrackCompletionListener(listener: () -> Unit) {
        repository.setOnCompletionListener(listener)
    }

    override fun getPlayerState(): PlayerState {
        return repository.getPlayerState()
    }

    override fun getCurrentPosition(): Int {
        return repository.getCurrentPosition()
    }


}