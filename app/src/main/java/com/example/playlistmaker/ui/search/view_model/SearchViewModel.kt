package com.example.playlistmaker.ui.search.view_model


import android.content.Context
import android.content.Intent
import android.util.Log

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.data.model.Track
import com.example.playlistmaker.domain.favorites.FavoritesInteractor
import com.example.playlistmaker.domain.model.SearchStatuses
import com.example.playlistmaker.domain.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.SearchInteractor
import com.example.playlistmaker.ui.player.fragment.TrackDetailFragment
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchViewModel(
    private val searchInteractor: SearchInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor,
    private val favoritesInteractor: FavoritesInteractor
) : ViewModel() {

    private var _searchStatusMutable = MutableLiveData<SearchStatuses>()
    val searchStatus: LiveData<SearchStatuses> = _searchStatusMutable

    private var _trackListMutable = MutableLiveData<List<Track>>()
    private var _trackHistoryListMutable = MutableLiveData<List<Track>>()
    private var isClickAllowed = true

    private var _isShowHistoryListMutable = MutableLiveData<Boolean>()
    val isShowHistoryList: LiveData<Boolean> = _isShowHistoryListMutable

    private var searchJob: Job? = null

    private val SEARCH_DEBOUNCE_DELAY = 2000L

    private val CLICK_DEBOUNCE_DELAY = 1000L

    private var latestSearchText: String? = null


    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }

        latestSearchText = changedText

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchTrack(changedText)
        }
    }


    fun setPaused() {
        searchJob?.cancel()
    }

    fun getTrackHistoryList(): LiveData<List<Track>> {
        return _trackHistoryListMutable
    }

    fun getTrackList(): LiveData<List<Track>> {
        return _trackListMutable
    }

    fun fillHistoryList() {
        searchHistoryInteractor.getHistoryList()
        _trackHistoryListMutable.value = searchHistoryInteractor.getTrackList()
    }

    fun clearSearchText() {
        _trackListMutable.value = listOf()
        _searchStatusMutable.value = SearchStatuses.SUCCESS

    }

    fun showHistoryList() {
        _trackListMutable.value = listOf()
        searchHistoryInteractor.getHistoryList()

        viewModelScope.launch {
            val trackList = searchHistoryInteractor.getTrackList()!!
            favoritesInteractor.fillMapForHistory(trackList)
            _trackHistoryListMutable.postValue(trackList)
        }

        _searchStatusMutable.value = SearchStatuses.SUCCESS
        _isShowHistoryListMutable.value = true
    }

    fun hideHistoryList() {
        _trackHistoryListMutable.value = listOf()
        _isShowHistoryListMutable.value = false
    }

    fun clearHistory() {
        searchHistoryInteractor.clearHistory()
        _trackHistoryListMutable.value = listOf()
    }

    fun clickTrack(trackId: Int): Track {

        var track = _trackListMutable.value?.find { it.trackId == trackId }
        if (track != null) {
            searchHistoryInteractor.addTrack(track)
        } else {
            track =
                searchHistoryInteractor.getTrackList().find {
                    it.trackId == trackId
                }
        }

        track!!.isInFavorites = favoritesInteractor.checkTrackForFavorites(track)

        return track!!
    }

    fun searchTrack(queryString: String) {
        _searchStatusMutable.value = SearchStatuses.IN_PROGRESS
        viewModelScope.launch {
            searchInteractor.searchTrack(queryString)
                .collect { foundTracks: List<Track>? ->
                    if (foundTracks == null) {
                        _searchStatusMutable.postValue(SearchStatuses.CONNECTION_ERROR)
                    }
                    if (foundTracks != null) {
                        if (foundTracks.isEmpty()) {
                            _searchStatusMutable.postValue(SearchStatuses.EMPTY_RESULT)
                        } else if (foundTracks.isNotEmpty()) {

                            favoritesInteractor.fillMapForSearch(foundTracks!!, searchHistoryInteractor.getTrackList())
                            _trackListMutable.postValue(foundTracks!!)

                            _searchStatusMutable.postValue(SearchStatuses.SUCCESS)
                        }
                    }

                }
        }
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