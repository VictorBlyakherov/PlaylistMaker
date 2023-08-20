package com.example.playlistmaker

import android.content.SharedPreferences
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

private const val MAX_TRACK_IN_HISTORY: Int = 10
private const val HISTORY_KEY = "historyList"

class SearchHistory(private val sharedPrefs: SharedPreferences) {

    var trackHistoryList: MutableList<Track> = ArrayList()

    fun addTrack(track: Track) {
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

    fun getHistoryList() {
        val historyList = sharedPrefs.getString(HISTORY_KEY, "")
        val typeToken = object : TypeToken<MutableList<Track>>() {}.type
        trackHistoryList.clear()
        if (!historyList.isNullOrEmpty()) {
            val trackList: ArrayList<Track> = Gson().fromJson(historyList, typeToken)
            trackHistoryList.addAll(trackList)
        }
    }

    fun clearHistory() {
        sharedPrefs.edit().putString(HISTORY_KEY, "").apply()
    }


}