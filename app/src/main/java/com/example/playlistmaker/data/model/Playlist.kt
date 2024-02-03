package com.example.playlistmaker.data.model

import java.io.Serializable

data class Playlist(
    val playlistId: Int,
    var playlistName: String,
    var playlistDescription: String,
    var coverFileName: String,
    var trackIdList: List<Int>,
    var trackCount: Int,
) : Serializable