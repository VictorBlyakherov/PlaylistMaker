package com.example.playlistmaker.ui.media.view_model

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.data.model.Track
import com.example.playlistmaker.domain.favorites.FavoritesInteractor
import com.example.playlistmaker.domain.model.FavoritesState
import com.example.playlistmaker.ui.player.fragment.TrackDetailFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FavoritesFragmentViewModel(
    private val favoritesInteractor: FavoritesInteractor
    ) : ViewModel() {

    private var _favoriteListMutable = MutableLiveData<List<Track>>()
    val _favoriteList: LiveData<List<Track>> = _favoriteListMutable

    private var _favoritesStatusMutable = MutableLiveData<FavoritesState>()
    val favoritesStatus: LiveData<FavoritesState> = _favoritesStatusMutable

    private var isClickAllowed = true

    private val CLICK_DEBOUNCE_DELAY = 1000L


    init {

    }

    fun fillFavoritesList(){
        viewModelScope.launch {
            _favoriteListMutable.value = listOf<Track>()
            favoritesInteractor.getFavoritesTracks().collect { it ->
                if (it == null) {
                    _favoritesStatusMutable.postValue(FavoritesState.EMPTY_RESULT)
                }
                if (it != null) {
                    if (it.isEmpty()) {
                        _favoritesStatusMutable.postValue(FavoritesState.EMPTY_RESULT)
                    } else if (it.isNotEmpty()) {
                        _favoriteListMutable.postValue(it!!)
                        _favoritesStatusMutable.postValue(FavoritesState.SUCCESS)
                    }
                }
            }
        }
    }

    fun clickTrack(trackId: Int): Track {
        val track = _favoriteListMutable.value?.find { it.trackId == trackId }
        return track!!
    }

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewModelScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }

        }
        return current
    }



}