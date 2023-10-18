package com.example.playlistmaker.data.search


import com.example.playlistmaker.domain.model.Track

interface SearchRepository {
    fun searchTrack(queryStr: String): List<Track>?
}