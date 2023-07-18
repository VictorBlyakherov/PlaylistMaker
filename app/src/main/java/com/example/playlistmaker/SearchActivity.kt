package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Converter.Factory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import kotlin.random.Random

class SearchActivity : AppCompatActivity() {

    private var searchText: String = ""
    private lateinit var inputEditText: EditText
    private lateinit var buttonBack: ImageButton
    private lateinit var clearButton: ImageView
    private lateinit var notFoundImage: ImageView
    private lateinit var notFoundText: TextView
    private lateinit var technicalError: ImageView
    private lateinit var technicalErrorText: TextView
    private lateinit var technicalErrorButton: Button

    private val trackList: MutableList<Track> = ArrayList()

    private val appleBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(appleBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val appleTrackService = retrofit.create(AppleMusicApi::class.java)

    private fun showNotFoundElements(){
        notFoundImage.visibility = View.VISIBLE
        notFoundText.visibility = View.VISIBLE
    }

    private fun showTechnicalErrorElements(){
        technicalError.visibility = View.VISIBLE
        technicalErrorText.visibility = View.VISIBLE
        technicalErrorButton.visibility = View.VISIBLE
    }

    private fun hideNotFoundElements(){
        notFoundImage.visibility = View.GONE
        notFoundText.visibility = View.GONE
    }

    private fun hideTechnicalErrorElements(){
        technicalError.visibility = View.GONE
        technicalErrorText.visibility = View.GONE
        technicalErrorButton.visibility = View.GONE
    }



    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
//        fillTrackList()
        inputEditText = findViewById(R.id.searchEditText)

        buttonBack = findViewById(R.id.backSearch)

        notFoundImage = findViewById(R.id.notFound)
        notFoundText = findViewById(R.id.notFoundText)

        technicalError = findViewById(R.id.technicalError)
        technicalErrorText = findViewById(R.id.technicalErrorText)
        technicalErrorButton = findViewById(R.id.technicalErrorButton)



        buttonBack.setOnClickListener {
            this.finish()
        }


        clearButton = findViewById<ImageView>(R.id.clearIcon)

        val recycler = findViewById<RecyclerView>(R.id.trackList)
        val adapter = TrackAdapter(trackList)

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter


        clearButton.setOnClickListener {
            inputEditText.setText("")
            trackList.clear()
            adapter.notifyDataSetChanged()
            hideNotFoundElements()
            hideTechnicalErrorElements()

            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)

        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                searchText = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)

        fun searchTrack() {
            if (searchText.isNotEmpty()) {
                trackList.clear()
                adapter.notifyDataSetChanged()
                appleTrackService.findTrack(searchText).enqueue(object : Callback<TrackResponse> {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onResponse(call: Call<TrackResponse>,
                                            response: Response<TrackResponse>) {
                        if (response.code() == 200) {


                            if (response.body()?.results?.isNotEmpty() == true) {
                                Log.d("resp1", "Not empty list")
                                trackList.addAll(response.body()?.results!!)
                                adapter.notifyDataSetChanged()
                                hideNotFoundElements()
                                hideTechnicalErrorElements()
                            }
                            if (trackList.isEmpty()) {
                                showNotFoundElements()
                            }
                        } else {
                            showTechnicalErrorElements()
                        }
                    }

                    override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                        showTechnicalErrorElements()
                    }

                })
            }
        }


        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchTrack()
            }
            false
        }

        technicalErrorButton.setOnClickListener {
            searchTrack()
        }


    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT,searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString(SEARCH_TEXT,"")
        inputEditText.setText(searchText)
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
