package com.example.playlistmaker.data.search.impl

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.data.search.SearchHistoryRepository

import com.example.playlistmaker.data.model.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class SearchHistoryImpl(private val context: Context) : SearchHistoryRepository {

    private val trackHistoryList: MutableList<Track> = ArrayList()
    private val sharedPrefs: SharedPreferences =
        context.getSharedPreferences(PLAYLIST_HISTORY, AppCompatActivity.MODE_PRIVATE)

    override fun addTrack(track: Track) {
        val historyList = sharedPrefs.getString(HISTORY_KEY, "")

        if (historyList.isNullOrEmpty()) {
            trackHistoryList.add(track)
        } else {

            val typeToken = object : TypeToken<MutableList<Track>>() {}.type
            trackHistoryList.clear()
            trackHistoryList.addAll(Gson().fromJson<MutableList<Track>>(historyList, typeToken))

            if (trackHistoryList.contains(track)) {
                trackHistoryList.remove(track)
                trackHistoryList.add(0, track)
            } else {
                if (trackHistoryList.size == MAX_TRACK_IN_HISTORY) {
                    trackHistoryList.removeAt(MAX_TRACK_IN_HISTORY - 1)
                    trackHistoryList.add(0, track)
                } else {
                    trackHistoryList.add(0, track)
                }
            }
        }

        sharedPrefs.edit().putString(HISTORY_KEY, Gson().toJson(trackHistoryList)).apply()
    }

    override fun getHistoryList() {
        val historyList = sharedPrefs.getString(HISTORY_KEY, "")
        val typeToken = object : TypeToken<MutableList<Track>>() {}.type
        trackHistoryList.clear()
        if (!historyList.isNullOrEmpty()) {
            val trackList: ArrayList<Track> = Gson().fromJson(historyList, typeToken)
            trackHistoryList.addAll(trackList)
        }
    }

    override fun clearHistory() {
        clearTrackList()
        sharedPrefs.edit().putString(HISTORY_KEY, "").apply()
    }

    override fun clearTrackList() {
        trackHistoryList.clear()
    }

    override fun getTrackList(): MutableList<Track> {
        return trackHistoryList
    }

    companion object {
        private val MAX_TRACK_IN_HISTORY: Int = 10
        private val HISTORY_KEY = "historyList"
        private val PLAYLIST_HISTORY = "playlist_history"
    }


}