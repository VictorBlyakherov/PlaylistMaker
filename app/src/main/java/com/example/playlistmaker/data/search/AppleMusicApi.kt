package com.example.playlistmaker.data.search

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AppleMusicApi {
    @GET("/search?entity=song")
    suspend fun findTrack(@Query("term") text: String): TrackSearchResponse
}