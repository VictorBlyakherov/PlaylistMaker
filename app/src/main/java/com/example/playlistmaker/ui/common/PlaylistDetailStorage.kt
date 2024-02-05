package com.example.playlistmaker.ui.common

import com.example.playlistmaker.data.model.Playlist
import com.example.playlistmaker.data.model.Track

interface PlaylistDetailStorage {
    fun setPlaylistDetail(playlistDetailArg: Playlist)
    fun getPlaylistDetail(): Playlist
}