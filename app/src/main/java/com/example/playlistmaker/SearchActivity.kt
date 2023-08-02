package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.opengl.Visibility
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.ActivitySearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

enum class SearchStatuses {
    SUCCESS, EMPTY_RESULT, CONNECTION_ERROR
}

const val PLAYLIST_HISTORY = "playlist_history"

class SearchActivity : AppCompatActivity() {
    private lateinit var sharedPrefs: SharedPreferences

    private lateinit var searchHistory: SearchHistory

    private var searchText: String = ""

    private val RESPONSEOKCODE: Int = 200

    private lateinit var binding: ActivitySearchBinding

    private val trackList: MutableList<Track> = ArrayList()

    private val appleBaseUrl = "https://itunes.apple.com"

    private lateinit var adapter: TrackAdapter

    private lateinit var adapterHistory: TrackAdapter

    private val retrofit =
        Retrofit.Builder().baseUrl(appleBaseUrl).addConverterFactory(GsonConverterFactory.create())
            .build()

    private val appleTrackService = retrofit.create(AppleMusicApi::class.java)

    fun onTrackClick(view: View?) {
        val trackId = view?.findViewById<TextView>(R.id.trackId)?.text
        if (!trackId.isNullOrEmpty()) {
            val track = trackList.find { it.trackId == trackId.toString().toInt() }
            if (track != null) {
                searchHistory.addTrack(track)
            }
        }
    }

    private fun setElements(status: SearchStatuses) {
        if (status == SearchStatuses.SUCCESS) {
            binding.technicalError.visibility = View.GONE
            binding.technicalErrorText.visibility = View.GONE
            binding.technicalErrorButton.visibility = View.GONE
        } else if (status == SearchStatuses.CONNECTION_ERROR) {
            binding.technicalError.setImageResource(R.drawable.technical_error)
            binding.technicalErrorText.setText(R.string.techError)
            binding.technicalError.visibility = View.VISIBLE
            binding.technicalErrorText.visibility = View.VISIBLE
            binding.technicalErrorButton.visibility = View.VISIBLE
        } else if (status == SearchStatuses.EMPTY_RESULT) {
            binding.technicalError.setImageResource(R.drawable.not_found_png)
            binding.technicalErrorText.setText(R.string.notFound)
            binding.technicalError.visibility = View.VISIBLE
            binding.technicalErrorText.visibility = View.VISIBLE
            binding.technicalErrorButton.visibility = View.GONE
        }
    }

    private fun clearButtonOnClick() {
        binding.searchEditText.setText("")
        trackList.clear()
        adapter.notifyDataSetChanged()
        setElements(SearchStatuses.SUCCESS)

        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)

    }

    private fun searchTrack() {
        if (searchText.isNotEmpty()) {
            trackList.clear()
            adapter.notifyDataSetChanged()
            appleTrackService.findTrack(searchText).enqueue(object : Callback<TrackResponse> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<TrackResponse>, response: Response<TrackResponse>
                ) {
                    if (response.code() == RESPONSEOKCODE) {
                        if (response.body()?.results?.isNotEmpty() == true) {
                            trackList.addAll(response.body()?.results!!)
                            adapter.notifyDataSetChanged()
                            setElements(SearchStatuses.SUCCESS)
                        }
                        if (trackList.isEmpty()) {
                            setElements(SearchStatuses.EMPTY_RESULT)
                        }
                    } else {
                        setElements(SearchStatuses.CONNECTION_ERROR)
                    }
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    setElements(SearchStatuses.CONNECTION_ERROR)
                }
            })
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPrefs = getSharedPreferences(PLAYLIST_HISTORY, MODE_PRIVATE)

        searchHistory = SearchHistory(sharedPrefs)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.backSearch.setOnClickListener {
            this.finish()
        }

        adapter = TrackAdapter(trackList)

        searchHistory.getHistoryList()
        adapterHistory = TrackAdapter(searchHistory.trackHistoryList)

        binding.trackList.layoutManager = LinearLayoutManager(this)
        binding.trackList.adapter = adapter

        binding.historyList.layoutManager = LinearLayoutManager(this)
        binding.historyList.adapter = adapterHistory


        binding.clearIcon.setOnClickListener {
            clearButtonOnClick()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearIcon.visibility = clearButtonVisibility(s)
                searchText = s.toString()
                if (binding.searchEditText.hasFocus() && s.isNullOrEmpty()) {
                    showTrackHistory()
                } else {
                    hideTrackHistory()
                }

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
            searchHistory.clearHistory()
            hideTrackHistory()
        }


        binding.searchEditText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && binding.searchEditText.text.isEmpty()) {
                showTrackHistory()
            } else {
                hideTrackHistory()
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
        trackList.clear()
        searchHistory.getHistoryList()
        adapterHistory.notifyDataSetChanged()
        setElements(SearchStatuses.SUCCESS)
        if (adapterHistory.itemCount > 0) {
            binding.clearHistoryButton.visibility = View.VISIBLE
            binding.searchHistoryText.visibility = View.VISIBLE
            binding.historyList.visibility = View.VISIBLE
        }
        binding.trackList.visibility = View.GONE
    }

    private fun hideTrackHistory() {
        searchHistory.trackHistoryList.clear()
        adapterHistory.notifyDataSetChanged()
        binding.clearHistoryButton.visibility = View.GONE
        binding.searchHistoryText.visibility = View.GONE
        binding.historyList.visibility = View.GONE
        binding.trackList.visibility = View.VISIBLE
    }


    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
    }


}