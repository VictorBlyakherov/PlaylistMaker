package com.example.playlistmaker.domain.search

import com.example.playlistmaker.data.model.Track

import kotlinx.coroutines.flow.Flow

interface SearchInteractor {
    fun searchTrack(queryString: String): Flow<List<Track>?>
}