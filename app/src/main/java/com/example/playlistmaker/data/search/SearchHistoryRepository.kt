package com.example.playlistmaker.data.search

import com.example.playlistmaker.domain.model.Track

interface SearchHistoryRepository {
    fun addTrack(track: Track)
    fun getHistoryList()
    fun clearHistory()
    fun clearTrackList()
    fun getTrackList(): MutableList<Track>

}