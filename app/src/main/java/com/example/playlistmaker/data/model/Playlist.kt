package com.example.playlistmaker.data.model

import java.io.Serializable

data class Playlist(
    val playlistId: Int,
    val playlistName: String,
    val playlistDescription: String,
    val coverFileName: String,
    var trackIdList: List<Int>,
    var trackCount: Int,
) : Serializable