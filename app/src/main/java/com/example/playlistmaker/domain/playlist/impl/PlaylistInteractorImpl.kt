package com.example.playlistmaker.domain.playlist.impl

import android.util.Log
import com.example.playlistmaker.data.model.Playlist
import com.example.playlistmaker.data.model.Track
import com.example.playlistmaker.data.playlist.PlaylistRepository
import com.example.playlistmaker.data.sharing.ExternalNavigator
import com.example.playlistmaker.domain.playlist.PlaylistInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository,
                             private val externalNavigator: ExternalNavigator,): PlaylistInteractor
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

    override suspend fun getPlaylistTrackList(tracksId: List<Int>): Flow<List<Track>> = flow {

        var allTrack: MutableList<Track> = mutableListOf()
        playlistRepository.getAllPlaylistTrack().collect {
            val listTemp = tracksId as MutableList<Long>

            for (track in it) {
                if (listTemp.contains(track.trackId.toLong())) {
                    allTrack.add(track)
                }
            }
        }

        emit(allTrack)
    }

    override suspend fun deleteTrackFromPlaylist(track: Track) {
        playlistRepository.deleteTrackFromPlaylist(track)
    }

    override fun sharePlaylist(shareText: String) {
        externalNavigator.shareLink(shareText)
    }

}