package com.example.playlistmaker.data.search


import com.example.playlistmaker.data.model.Track

import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun searchTrack(queryStr: String): Flow<List<Track>?>
}