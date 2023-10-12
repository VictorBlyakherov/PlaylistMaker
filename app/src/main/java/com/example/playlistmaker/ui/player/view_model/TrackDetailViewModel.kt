package com.example.playlistmaker.ui.player.view_model

import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.model.PlayerState
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.player.PlayTrackInteractor


class TrackDetailViewModel (private val playTrackInteractor: PlayTrackInteractor) : ViewModel() {

    private val PLAY_DEBOUNCE_DELAY = 1000L


    private var playIconMutable = MutableLiveData<String>()
    val playIcon: LiveData<String> = playIconMutable

    private var isPlayEnabledMutable = MutableLiveData<Boolean>()
    val isPlayEnable: LiveData<Boolean> = isPlayEnabledMutable

    private var currentPlayPositionMutable = MutableLiveData<Int>()
    val currentPlayPosition: LiveData<Int> = currentPlayPositionMutable

    private var trackMutable = MutableLiveData<Track>()
    val track: LiveData<Track> = trackMutable

    private val handler = Handler(Looper.getMainLooper())
    private val timeChangeRunnable = increasePlayTime()

    private fun increasePlayTime() :Runnable {

        return object : Runnable {
            override fun run() {
                currentPlayPositionMutable.value = currentPlayPositionMutable.value?.plus(1)
                handler.postDelayed(this, PLAY_DEBOUNCE_DELAY)
            }
        }
    }



    fun preparePlayer(intent: Intent){
        trackMutable.value = intent.getSerializableExtra("track") as? Track

        var trackUrl = ""
        trackUrl = if (trackMutable.value?.previewUrl == null) {
            ""
        } else {
            trackMutable.value?.previewUrl
        }!!

        playIconMutable.value = "play"
        handler.removeCallbacks(timeChangeRunnable)
        isPlayEnabledMutable.value = true
        playTrackInteractor.preparePlayer(trackUrl)
        currentPlayPositionMutable.value = 0


        playTrackInteractor.setTrackCompletionListener {
            playIconMutable.value = "play"
            currentPlayPositionMutable.value = 0
            handler.removeCallbacks(timeChangeRunnable)
        }

    }

    fun pausePlayer() {
        playTrackInteractor.pausePlayer()
    }

    fun stopPlayer() {
        playTrackInteractor.stopPlayer()
    }

    fun playbackControl() {
        when (playTrackInteractor.getPlayerState()) {
            PlayerState.STATE_PAUSED, PlayerState.STATE_PREPARED, PlayerState.STATE_DEFAULT -> {
                handler.removeCallbacks(timeChangeRunnable)
                playIconMutable.value = "pause"
                playTrackInteractor.startPlayer()
                handler.postDelayed(timeChangeRunnable, PLAY_DEBOUNCE_DELAY)
            }

            PlayerState.STATE_PLAYING -> {
                handler.removeCallbacks(timeChangeRunnable)
                playIconMutable.value = "play"
                playTrackInteractor.pausePlayer()
            }
        }

    }


    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return TrackDetailViewModel(
                        Creator.providePlayTrackInteractor()
                    ) as T
                }
            }
    }

}