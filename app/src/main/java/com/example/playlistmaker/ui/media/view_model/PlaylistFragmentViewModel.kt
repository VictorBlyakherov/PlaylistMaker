package com.example.playlistmaker.ui.media.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.data.model.Playlist
import com.example.playlistmaker.data.model.Track
import com.example.playlistmaker.domain.model.FavoritesState
import com.example.playlistmaker.domain.playlist.PlaylistInteractor
import kotlinx.coroutines.launch

class PlaylistFragmentViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {

    private var _playlistListMutable = MutableLiveData<List<Playlist>>()
    val playlistList: LiveData<List<Playlist>> = _playlistListMutable

    init {

    }

    fun getPlaylists() {
        viewModelScope.launch {
            playlistInteractor.getPlaylistList().collect { it ->
                if (!it.isNullOrEmpty()) {
                    _playlistListMutable.postValue(it)
                } else {
                    _playlistListMutable.postValue(listOf())
                }

            }
        }
    }


}