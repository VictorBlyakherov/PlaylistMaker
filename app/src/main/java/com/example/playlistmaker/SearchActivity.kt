package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
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

class SearchActivity : AppCompatActivity() {

    private var searchText: String = ""

    private val RESPONSE_OK_CODE: Int = 200

    private lateinit var binding: ActivitySearchBinding

    private val trackList: MutableList<Track> = ArrayList()

    private val appleBaseUrl = "https://itunes.apple.com"

    private lateinit var adapter: TrackAdapter

    private val retrofit =
        Retrofit.Builder().baseUrl(appleBaseUrl).addConverterFactory(GsonConverterFactory.create())
            .build()

    private val appleTrackService = retrofit.create(AppleMusicApi::class.java)

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
                    if (response.code() == RESPONSE_OK_CODE) {
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
        binding = ActivitySearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.backSearch.setOnClickListener {
            this.finish()
        }

        adapter = TrackAdapter(trackList)

        binding.trackList.layoutManager = LinearLayoutManager(this)
        binding.trackList.adapter = adapter


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

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
    }

}