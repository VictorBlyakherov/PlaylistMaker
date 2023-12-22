package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.data.model.Track
import com.example.playlistmaker.data.search.SearchRepository
import com.example.playlistmaker.domain.search.SearchInteractor
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.Executors

class SearchInteractorImpl(private val repository: SearchRepository) : SearchInteractor {

    override fun searchTrack(queryString: String): Flow<List<Track>?> {
       return repository.searchTrack(queryString)
    }
}