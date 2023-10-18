package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.data.search.SearchHistoryRepository
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.search.SearchHistoryInteractor

class SearchHistoryInteractorImpl(private val repository: SearchHistoryRepository) :
    SearchHistoryInteractor {
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