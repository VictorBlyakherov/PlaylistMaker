package com.example.playlistmaker.ui.search.activity


import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher

import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R

import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.model.SearchStatuses
import com.example.playlistmaker.domain.search.SearchHistoryInteractor
import com.example.playlistmaker.ui.search.view_model.SearchViewModel


const val PLAYLIST_HISTORY = "playlist_history"

class SearchActivity : AppCompatActivity() {

    private lateinit var searchViewModel: SearchViewModel

    private val CLICK_DEBOUNCE_DELAY = 1000L

    private val SEARCH_DEBOUNCE_DELAY = 2000L

    private val searchRunnable = Runnable { searchTrack() }

    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())

    private lateinit var sharedPrefs: SharedPreferences

    private lateinit var searchHistory: SearchHistoryInteractor


    private var searchText: String = ""

    private lateinit var binding: ActivitySearchBinding

    private lateinit var adapter: TrackAdapter

    private lateinit var adapterHistory: TrackAdapter


    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }


    private fun setElements(status: SearchStatuses) {
        if (status == SearchStatuses.SUCCESS) {
            binding.progressBar.visibility = View.GONE
            binding.technicalError.visibility = View.GONE
            binding.technicalErrorText.visibility = View.GONE
            binding.technicalErrorButton.visibility = View.GONE
            binding.trackList.visibility = View.VISIBLE
        } else if (status == SearchStatuses.CONNECTION_ERROR) {
            binding.progressBar.visibility = View.GONE
            binding.technicalError.setImageResource(R.drawable.technical_error)
            binding.technicalErrorText.setText(R.string.techError)
            binding.technicalError.visibility = View.VISIBLE
            binding.technicalErrorText.visibility = View.VISIBLE
            binding.technicalErrorButton.visibility = View.VISIBLE
            binding.trackList.visibility = View.GONE
        } else if (status == SearchStatuses.EMPTY_RESULT) {
            binding.progressBar.visibility = View.GONE
            binding.technicalError.setImageResource(R.drawable.not_found_png)
            binding.technicalErrorText.setText(R.string.notFound)
            binding.technicalError.visibility = View.VISIBLE
            binding.technicalErrorText.visibility = View.VISIBLE
            binding.technicalErrorButton.visibility = View.GONE
            binding.trackList.visibility = View.GONE
        } else if (status == SearchStatuses.IN_PROGRESS) {
            binding.progressBar.visibility = View.VISIBLE
            binding.technicalError.visibility = View.GONE
            binding.technicalErrorText.visibility = View.GONE
            binding.technicalErrorButton.visibility = View.GONE
            binding.trackList.visibility = View.GONE
        }
    }


    private fun searchTrack() {

        if (searchText.isNotEmpty()) {
            searchViewModel.searchTrack(searchText)
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPrefs = getSharedPreferences(PLAYLIST_HISTORY, MODE_PRIVATE)

        searchHistory = Creator.provideSearchHistoryInteractor(sharedPrefs)

        searchViewModel = ViewModelProvider(
            this,
            SearchViewModel.getViewModelFactory(sharedPrefs)
        )[SearchViewModel::class.java]


        binding = ActivitySearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)



        binding.backSearch.setOnClickListener {
            this.finish()
        }

        binding.clearIcon.setOnClickListener {
            searchViewModel.clearSearchText()
            binding.searchEditText.setText("")
        }

        searchViewModel.searchStatus.observe(this) {
            setElements(it)
        }

        searchViewModel.getTrackList().observe(this) {trackList ->
            adapter = TrackAdapter ({
                        onTrackClick(it)
                    }, trackList)

            binding.trackList.layoutManager = LinearLayoutManager(this)
            binding.trackList.adapter = adapter
            adapter.notifyDataSetChanged()
        }



        searchViewModel.getTrackHistoryList().observe(this) {trackList ->
            adapterHistory = TrackAdapter({
                onTrackClick(it)
            }, trackList)

            binding.historyList.layoutManager = LinearLayoutManager(this)
            binding.historyList.adapter = adapterHistory
            adapterHistory.notifyDataSetChanged()
    }

        searchViewModel.isShowHistoryList.observe(this) {
            if (it) {
                showTrackHistory()
            } else {
                hideTrackHistory()
            }
        }

        searchViewModel.fillHistoryList()

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearIcon.visibility = clearButtonVisibility(s)
                searchText = s.toString()
                if (binding.searchEditText.hasFocus() && s.isNullOrEmpty()) {
                    searchViewModel.showHistoryList()
                } else {
                    searchViewModel.hideHistoryList()
                }

                searchDebounce()
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }

        binding.searchEditText.addTextChangedListener(simpleTextWatcher)

        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchTrack()
            }
            false
        }

        binding.technicalErrorButton.setOnClickListener {
            searchTrack()
        }

        binding.clearHistoryButton.setOnClickListener {
            searchViewModel.clearHistory()
            searchViewModel.hideHistoryList()
        }


        binding.searchEditText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && binding.searchEditText.text.isEmpty()) {
                searchViewModel.showHistoryList()
            } else {
                searchViewModel.hideHistoryList()
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString(SEARCH_TEXT, "")
        binding.searchEditText.setText(searchText)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun showTrackHistory() {
        if (adapterHistory.itemCount > 0) {
            binding.clearHistoryButton.visibility = View.VISIBLE
            binding.searchHistoryText.visibility = View.VISIBLE
            binding.historyList.visibility = View.VISIBLE
        }
        binding.trackList.visibility = View.GONE
    }

    private fun hideTrackHistory() {
        binding.clearHistoryButton.visibility = View.GONE
        binding.searchHistoryText.visibility = View.GONE
        binding.historyList.visibility = View.GONE
        binding.trackList.visibility = View.VISIBLE
    }

    fun onTrackClick(trackId: Int) {
        if (clickDebounce()) {
            searchViewModel.clickTrack(this, trackId)
        }
    }

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
    }


}