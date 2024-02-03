package com.example.playlistmaker.ui.media.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.data.model.Playlist
import com.example.playlistmaker.data.model.Track
import com.example.playlistmaker.domain.playlist.PlaylistInteractor
import kotlinx.coroutines.launch

class PlaylistDetailFragmentViewModel(private val playlistInteractor: PlaylistInteractor):ViewModel() {
    private var _trackListMutable = MutableLiveData<List<Track>>()
    val trackList: LiveData<List<Track>> = _trackListMutable

    private var playlistTrackDuration: Long = 0

    fun getPlaylistTrackDuration(): Long {
        return  playlistTrackDuration
    }

    fun getTrackList(playlist: Playlist) {

        viewModelScope.launch {
            playlistInteractor.getPlaylistTrackList(playlist.trackIdList).collect { it ->

                if (!it.isNullOrEmpty()) {
                    playlistTrackDuration = calcDuration(it)
                    _trackListMutable.postValue(it)
                } else {
                    _trackListMutable.postValue(listOf())
                }

            }
        }
    }

    private fun calcDuration(tracks: List<Track>): Long {
        var trackDuration: Long = 0
        for (track in tracks) {
            trackDuration += track.trackTimeMillis
        }
        return trackDuration
    }

    fun clickTrack(trackId: Int): Track {
        val track = _trackListMutable.value?.find { it.trackId == trackId }
        return track!!
    }

    fun deleteTrack(track: Track, playlist: Playlist): Playlist {
        playlist.trackCount = playlist.trackCount - 1
        val tempList = playlist.trackIdList as MutableList<Long>
        tempList.remove(track.trackId.toLong())
        playlist.trackIdList = tempList as List<Int>
        viewModelScope.launch {
            playlistInteractor.updatePlaylist(playlist)
            playlistInteractor.deleteTrackFromPlaylist(track)
        }

        val tempTrackList = _trackListMutable.value as MutableList<Track>

        tempTrackList.remove(track)
        playlistTrackDuration = calcDuration(tempTrackList)
        _trackListMutable.postValue(tempTrackList)

        return playlist

    }

    fun sharePlaylist(strShare: String) {
        playlistInteractor.sharePlaylist(strShare)
    }

    fun deletePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistInteractor.deletePlaylist(playlist)
            for (track in _trackListMutable.value!!) {
                playlistInteractor.deleteTrackFromPlaylist(track)
            }
        }
    }


}