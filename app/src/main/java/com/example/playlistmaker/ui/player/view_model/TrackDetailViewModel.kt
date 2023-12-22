package com.example.playlistmaker.ui.player.view_model

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.model.PlayerState
import com.example.playlistmaker.domain.model.PlayingStatus
import com.example.playlistmaker.data.model.Track
import com.example.playlistmaker.domain.favorites.FavoritesInteractor
import com.example.playlistmaker.domain.player.PlayTrackInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale


class TrackDetailViewModel(private val playTrackInteractor: PlayTrackInteractor, private val favoritesInteractor: FavoritesInteractor) : ViewModel() {

    private var timerJob: Job? = null

    private val PLAY_DEBOUNCE_DELAY = 300L

    private var _playingStatusMutable = MutableLiveData<PlayingStatus>()
    val playingStatus: LiveData<PlayingStatus> = _playingStatusMutable

    private var _currentPlayPositionMutable = MutableLiveData<String>()
    val currentPlayPosition: LiveData<String> = _currentPlayPositionMutable

    private var _trackMutable = MutableLiveData<Track>()
    val track: LiveData<Track> = _trackMutable

    fun setFavorites() {
        if (_trackMutable.value!!.isInFavorites) {
            viewModelScope.launch { favoritesInteractor.deleteFromFavorites(_trackMutable.value!!) }

        } else {
            Log.d("AAAA", "2")
            viewModelScope.launch { favoritesInteractor.addToFavorites(_trackMutable.value!!) }

        }

        _trackMutable.value!!.isInFavorites = !_trackMutable.value!!.isInFavorites

    }

    fun preparePlayer(intent: Intent) {
        _trackMutable.value = intent.getSerializableExtra("track") as? Track

        var trackUrl = ""
        trackUrl = if (_trackMutable.value?.previewUrl == null) {
            ""
        } else {
            _trackMutable.value?.previewUrl
        }!!

        _playingStatusMutable.value = PlayingStatus.PLAY
        playTrackInteractor.preparePlayer(trackUrl)
        _currentPlayPositionMutable.value = "00:00"



        playTrackInteractor.setTrackCompletionListener {
            _playingStatusMutable.value = PlayingStatus.PLAY
            timerJob?.cancel()
            _currentPlayPositionMutable.value = "00:00"

        }

    }

    fun pausePlayer() {
        playTrackInteractor.pausePlayer()
        timerJob?.cancel()
    }

    fun stopPlayer() {
        playTrackInteractor.stopPlayer()
        timerJob?.cancel()
        _currentPlayPositionMutable.value = "00:00"

    }

    fun startPlayer() {
        playTrackInteractor.startPlayer()
        startTimer()
    }


    fun playbackControl() {
        when (playTrackInteractor.getPlayerState()) {
            PlayerState.STATE_PAUSED, PlayerState.STATE_PREPARED, PlayerState.STATE_DEFAULT -> {
                _playingStatusMutable.value = PlayingStatus.PAUSE
                startPlayer()
            }

            PlayerState.STATE_PLAYING -> {
                _playingStatusMutable.value = PlayingStatus.PLAY
                pausePlayer()
            }
        }
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (playTrackInteractor.getPlayerState() == PlayerState.STATE_PLAYING) {
                delay(PLAY_DEBOUNCE_DELAY)
                _currentPlayPositionMutable.value = getCurrentPlayerPosition()
            }
        }
    }

    private fun getCurrentPlayerPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(playTrackInteractor.getCurrentPosition()) ?: "00:00"
    }
}