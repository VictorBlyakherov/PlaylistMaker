package com.example.playlistmaker.data.playlist.impl

import com.example.playlistmaker.data.converters.PlaylistDbConverter
import com.example.playlistmaker.data.converters.PlaylistTrackDbConverter
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.data.db.entity.TrackPlaylistEntity
import com.example.playlistmaker.data.model.Playlist
import com.example.playlistmaker.data.model.Track
import com.example.playlistmaker.data.playlist.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConverter: PlaylistDbConverter,
    private val playlistTrackDbConverter: PlaylistTrackDbConverter
): PlaylistRepository {

    override suspend fun getPlaylistList(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.playlistDao().getPlaylists()
        emit(convertFromPlaylistEntity(playlists))
    }

    override suspend fun addPlaylist(playlist: Playlist) {
        appDatabase.playlistDao().insertPlaylist(convertToTrackEntity(playlist))
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().deletePlaylist(convertToTrackEntity(playlist))
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().updatePlaylist(convertToTrackEntity(playlist))
    }

    override suspend fun insertTrackToPlTable(track: Track) {
        appDatabase.playlistDao().insertTrackToPlaylist(convertToTrackPlaylistEntity(track))
    }

    override suspend fun deleteTrackFromPlTable(track: Track) {
        appDatabase.playlistDao().deleteTrackFromPlaylist(convertToTrackPlaylistEntity(track))
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        val trackIdList: MutableList<Int> = playlist.trackIdList as MutableList<Int>
        trackIdList.add(track.trackId)
        playlist.trackIdList = trackIdList
        playlist.trackCount = playlist.trackCount + 1
        updatePlaylist(playlist)
        insertTrackToPlTable(track)
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> playlistDbConverter.map(playlist) }
    }

    private fun convertToTrackEntity(playlist: Playlist): PlaylistEntity {
        return playlistDbConverter.map(playlist)
    }

    private fun convertToTrackPlaylistEntity(track: Track): TrackPlaylistEntity {
        return playlistTrackDbConverter.map(track)
    }


}