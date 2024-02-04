package com.example.playlistmaker.ui.media.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.data.model.Playlist
import com.example.playlistmaker.domain.playlist.PlaylistInteractor
import kotlinx.coroutines.launch

class PlaylistEditFragmentViewModel(private val playlistInteractor: PlaylistInteractor): PlaylistAddFragmentViewModel(playlistInteractor)
{

    fun savePlaylist(playlist: Playlist) {
        if (!fileName.isNullOrEmpty())
        {
            playlist.coverFileName = fileName
        }
        viewModelScope.launch {
            playlistInteractor.updatePlaylist(playlist)
        }
    }

}