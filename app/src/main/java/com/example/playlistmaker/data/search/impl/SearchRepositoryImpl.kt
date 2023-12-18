package com.example.playlistmaker.data.search.impl

import com.example.playlistmaker.data.search.SearchRepository
import com.example.playlistmaker.data.search.TrackSearchResponse

import com.example.playlistmaker.data.search.dto.TrackSearchRequest
import com.example.playlistmaker.data.search.network.NetworkClient
import com.example.playlistmaker.data.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class SearchRepositoryImpl(private val networkClient: NetworkClient) : SearchRepository {
    override fun searchTrack(queryStr: String): Flow<List<Track>?> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(queryStr))

        if (response.resultCode == 200) {
            with (response as TrackSearchResponse) {
                val data = results.map {
                    Track(trackId = it.trackId,
                            trackName = it.trackName,
                            artistName = it.artistName,
                            trackTimeMillis = it.trackTimeMillis,
                            artworkUrl100 = it.artworkUrl100,
                            collectionName = it.collectionName,
                            releaseDate = it.releaseDate,
                            primaryGenreName = it.primaryGenreName,
                            country = it.country,
                            previewUrl = it.previewUrl
                        )
                }
                emit(data)
            }
        } else {
            emit(null)
        }

    }
}