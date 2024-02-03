package com.example.playlistmaker.data.converters

import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.data.model.Playlist
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.ToNumberPolicy
import com.google.gson.reflect.TypeToken


class PlaylistDbConverter {
    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlistId = playlist.playlistId,
            playlistName = playlist.playlistName,
            playlistDescription = playlist.playlistDescription,
            coverFileName = playlist.coverFileName,
            trackIdList = convertListToJson(playlist.trackIdList),
            trackCount = playlist.trackCount
        )
    }

    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(
            playlistId = playlist.playlistId,
            playlistName = playlist.playlistName,
            playlistDescription = playlist.playlistDescription,
            coverFileName = playlist.coverFileName,
            trackIdList = convertJsonToList(playlist.trackIdList),
            trackCount = playlist.trackCount
        )
    }

    private fun convertListToJson(trackIdList: List<Int>): String {
        val gsonBuilder = GsonBuilder().setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
        val gson = gsonBuilder.create()
        return gson.toJson(trackIdList, object : TypeToken<List<Int>?>() {}.type)
    }

    private fun convertJsonToList(trackIdList: String): List<Int> {
        val gsonBuilder = GsonBuilder().setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
        val gson = gsonBuilder.create()
        return gson.fromJson(trackIdList, List::class.java) as List<Int>
    }

}