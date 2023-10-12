package com.example.playlistmaker.ui.search.view_model


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.model.SearchStatuses
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.SearchInteractor
import com.example.playlistmaker.ui.player.activity.TrackDetailsActivity



class SearchViewModel(private val searchInteractor: SearchInteractor, private val searchHistoryInteractor:SearchHistoryInteractor): ViewModel() {

    private val RESPONSEOKCODE: Int = 200

    private var searchStatusMutable = MutableLiveData<SearchStatuses>()
    val searchStatus = searchStatusMutable
    private var trackListMutable = MutableLiveData<MutableList<Track>>()
    private var searchEditTextMutable = MutableLiveData<String>()
    val searchEditText = searchEditTextMutable
    private var trackHistoryListMutable = MutableLiveData<MutableList<Track>>()
    private var isShowHistoryListMutable = MutableLiveData<Boolean>()
    val isShowHistoryList = isShowHistoryListMutable

    fun getTrackHistoryList() : LiveData<MutableList<Track>> {
        return trackHistoryListMutable
    }

    fun getTrackList(): LiveData<MutableList<Track>> {
        return trackListMutable
    }

    fun fillHistoryList() {
        searchHistoryInteractor.getHistoryList()
        trackHistoryListMutable.value = searchHistoryInteractor.getTrackList()
    }

    fun clearSearchText() {
        searchEditTextMutable.value = ""
        trackListMutable.value?.clear()
        searchStatusMutable.value = SearchStatuses.SUCCESS

    }

    fun showHistoryList() {
        trackListMutable.value?.clear()
        searchHistoryInteractor.getHistoryList()
        trackHistoryListMutable.value = searchHistoryInteractor.getTrackList()
        searchStatus.value = SearchStatuses.SUCCESS
        isShowHistoryListMutable.value = true
    }

    fun hideHistoryList() {
        trackHistoryListMutable.value?.clear()
        isShowHistoryListMutable.value = false
    }

    fun clearHistory(){
        searchHistoryInteractor.clearHistory()
        trackHistoryListMutable.value?.clear()
    }

    fun clickTrack(context: Context, trackId: Int) {


        var track = trackListMutable.value?.find { it.trackId == trackId }
        if (track != null) {
            searchHistoryInteractor.addTrack(track)
        } else {
            track =
                searchHistoryInteractor.getTrackList().find {
                    it.trackId == trackId
                }
        }

        val displayIntent = Intent(context, TrackDetailsActivity::class.java)
        displayIntent.putExtra("track", track)
        context.startActivity(displayIntent)

    }

    fun searchTrack(queryString: String) {

        searchStatus.value = SearchStatuses.IN_PROGRESS
        val resp = searchInteractor.searchTrack(queryString, object:
            SearchInteractor.TrackConsumer {
                override fun consume(foundTracks: List<Track>?){
                    if (foundTracks == null) {
                        searchStatus.postValue(SearchStatuses.CONNECTION_ERROR)
                    }
                    if (foundTracks != null) {
                        if (foundTracks.isEmpty()) {
                            searchStatus.postValue(SearchStatuses.EMPTY_RESULT)
                        } else if (foundTracks.isNotEmpty()) {
                            val tempList = mutableListOf<Track>()
                            tempList.addAll(foundTracks)
                            trackListMutable.postValue(tempList)
                            searchStatusMutable.postValue(SearchStatuses.SUCCESS)
                        }
                    }
               }
        })
    }

    companion object {
        fun getViewModelFactory(sharedPreferences: SharedPreferences): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SearchViewModel(
                        Creator.provideSearchInteractor(),
                        Creator.provideSearchHistoryInteractor(sharedPreferences)
                    ) as T
                }
            }
    }

}