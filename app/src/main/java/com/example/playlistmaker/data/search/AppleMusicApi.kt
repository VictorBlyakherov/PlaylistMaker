package com.example.playlistmaker.data.search

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AppleMusicApi {
    @GET("/search?entity=song")
    fun findTrack(@Query("term") text: String): Call<TrackSearchResponse>
}