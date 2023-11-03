package com.example.playlistmaker.data.search.impl

import com.example.playlistmaker.data.search.SearchRepository
import com.example.playlistmaker.data.search.TrackSearchResponse

import com.example.playlistmaker.data.search.dto.TrackSearchRequest
import com.example.playlistmaker.data.search.network.NetworkClient
import com.example.playlistmaker.domain.model.Track


class SearchRepositoryImpl(private val networkClient: NetworkClient) : SearchRepository {
    override fun searchTrack(queryStr: String): List<Track>? {
        val response = networkClient.doRequest(TrackSearchRequest(queryStr))

        if (response.resultCode == 200) {
            return (response as TrackSearchResponse).results
        } else {
            return null
        }

    }
}