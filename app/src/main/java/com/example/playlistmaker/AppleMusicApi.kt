package com.example.playlistmaker

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AppleMusicApi {
    @GET("/search?")
    fun findTrack(@Query("term") text: String): Call<TrackResponse>
}