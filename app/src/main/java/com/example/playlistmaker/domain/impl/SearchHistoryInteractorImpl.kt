package com.example.playlistmaker.domain.impl

import android.content.SharedPreferences
import com.example.playlistmaker.data.api.PlayTrackRepository
import com.example.playlistmaker.data.api.SearchHistoryRepository
import com.example.playlistmaker.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.model.Track

class SearchHistoryInteractorImpl(private val repository: SearchHistoryRepository) :SearchHistoryInteractor {
    override fun addTrack(track: Track) {
        repository.addTrack(track)
    }

    override fun getHistoryList() {
        repository.getHistoryList()
    }

    override fun clearHistory() {
        repository.clearHistory()
    }

    override fun clearTrackList() {
        repository.clearTrackList()
    }

    override fun getTrackList() : MutableList<Track> {
        return repository.getTrackList()
    }

}