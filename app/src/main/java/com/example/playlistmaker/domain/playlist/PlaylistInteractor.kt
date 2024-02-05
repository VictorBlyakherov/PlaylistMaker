package com.example.playlistmaker.domain.playlist

import com.example.playlistmaker.data.model.Playlist
import com.example.playlistmaker.data.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    suspend fun getPlaylistList(): Flow<List<Playlist>>

    suspend fun addPlaylist(playlist: Playlist)

    suspend fun deletePlaylist(playlist: Playlist)

    suspend fun updatePlaylist(playlist: Playlist)

    suspend fun insertTrackToPlTable(track: Track)

    suspend fun deleteTrackFromPlTable(track: Track)

    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist)

    suspend fun getPlaylistTrackList(tracksId: List<Int>): Flow<List<Track>>

    suspend fun deleteTrackFromPlaylist(track: Track)

    fun sharePlaylist(shareText: String)

}