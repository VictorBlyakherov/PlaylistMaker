package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.model.Track

interface SearchInteractor {
    fun searchTrack(queryString: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(foundTracks: List<Track>?)
    }
}