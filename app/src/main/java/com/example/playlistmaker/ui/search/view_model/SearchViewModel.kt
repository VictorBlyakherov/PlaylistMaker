package com.example.playlistmaker.ui.search.view_model


import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.model.SearchStatuses
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.SearchInteractor
import com.example.playlistmaker.ui.player.activity.TrackDetailsActivity


class SearchViewModel(
    private val searchInteractor: SearchInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor
) : ViewModel() {

    private var _searchStatusMutable = MutableLiveData<SearchStatuses>()
    val searchStatus: LiveData<SearchStatuses> = _searchStatusMutable

    private var _trackListMutable = MutableLiveData<List<Track>>()
    private var _trackHistoryListMutable = MutableLiveData<List<Track>>()


    private var _isShowHistoryListMutable = MutableLiveData<Boolean>()
    val isShowHistoryList: LiveData<Boolean> = _isShowHistoryListMutable


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
        _trackHistoryListMutable.value = searchHistoryInteractor.getTrackList()
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

    fun clickTrack(context: Context, trackId: Int) {

        var track = _trackListMutable.value?.find { it.trackId == trackId }
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

        _searchStatusMutable.value = SearchStatuses.IN_PROGRESS
        val resp = searchInteractor.searchTrack(queryString, object :
            SearchInteractor.TrackConsumer {
            override fun consume(foundTracks: List<Track>?) {
                if (foundTracks == null) {
                    _searchStatusMutable.postValue(SearchStatuses.CONNECTION_ERROR)
                }
                if (foundTracks != null) {
                    if (foundTracks.isEmpty()) {
                        _searchStatusMutable.postValue(SearchStatuses.EMPTY_RESULT)
                    } else if (foundTracks.isNotEmpty()) {
                        _trackListMutable.postValue(foundTracks!!)
                        _searchStatusMutable.postValue(SearchStatuses.SUCCESS)
                    }
                }
            }
        })
    }

}