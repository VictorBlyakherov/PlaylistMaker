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
import com.example.playlistmaker.domain.model.SearchStatuses
import com.example.playlistmaker.ui.player.activity.TrackDetailsActivity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class FavoritesFragmentViewModel(
    private val favoritesInteractor: FavoritesInteractor
    ) : ViewModel() {

    private var _favoriteListMutable = MutableLiveData<List<Track>>()

    private var _favoritesStatusMutable = MutableLiveData<FavoritesState>()
    val favoritesStatus: LiveData<FavoritesState> = _favoritesStatusMutable



    init {

    }

    fun getFavoritesList():LiveData<List<Track>> {
        viewModelScope.launch {
            _favoriteListMutable.value = listOf<Track>()
            favoritesInteractor.getFavoritesTracks().collect {
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

        return _favoriteListMutable
    }

    fun clickTrack(context: Context, trackId: Int) {
        val track = _favoriteListMutable.value?.find { it.trackId == trackId }
        val displayIntent = Intent(context, TrackDetailsActivity::class.java)
        displayIntent.putExtra("track", track)
        context.startActivity(displayIntent)
    }



}