package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.data.search.SearchRepository
import com.example.playlistmaker.domain.search.SearchInteractor
import java.util.concurrent.Executors

class SearchInteractorImpl(private val repository: SearchRepository): SearchInteractor {
    private val executor = Executors.newCachedThreadPool()

    override fun searchTrack(queryString: String, consumer: SearchInteractor.TrackConsumer) {
        executor.execute {
            consumer.consume(repository.searchTrack(queryString))
        }
    }
}