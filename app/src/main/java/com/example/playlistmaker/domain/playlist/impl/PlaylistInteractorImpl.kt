package com.example.playlistmaker.domain.playlist.impl

import com.example.playlistmaker.data.model.Playlist
import com.example.playlistmaker.data.model.Track
import com.example.playlistmaker.data.playlist.PlaylistRepository
import com.example.playlistmaker.domain.playlist.PlaylistInteractor
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository): PlaylistInteractor
{
    override suspend fun getPlaylistList(): Flow<List<Playlist>> {
        return playlistRepository.getPlaylistList()
    }

    override suspend fun addPlaylist(playlist: Playlist) {
        playlistRepository.addPlaylist(playlist)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistRepository.deletePlaylist(playlist)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistRepository.updatePlaylist(playlist)
    }

    override suspend fun insertTrackToPlTable(track: Track) {
        playlistRepository.insertTrackToPlTable(track)
    }

    override suspend fun deleteTrackFromPlTable(track: Track) {
        playlistRepository.deleteTrackFromPlTable(track)
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        playlistRepository.addTrackToPlaylist(track, playlist)
    }

}