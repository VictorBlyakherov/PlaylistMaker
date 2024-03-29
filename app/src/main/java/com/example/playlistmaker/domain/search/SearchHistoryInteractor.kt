package com.example.playlistmaker.domain.search

import com.example.playlistmaker.data.model.Track

interface SearchHistoryInteractor {
    fun addTrack(track: Track)
    fun getHistoryList()
    fun clearHistory()
    fun clearTrackList()
    fun getTrackList(): MutableList<Track>

}