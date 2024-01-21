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
            playlist.playlistId,
            playlist.playlistName,
            playlist.playlistDescription,
            playlist.coverFileName,
            convertListToJson(playlist.trackIdList),
            playlist.trackCount
        )
    }

    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(
            playlist.playlistId,
            playlist.playlistName,
            playlist.playlistDescription,
            playlist.coverFileName,
            convertJsonToList(playlist.trackIdList),
            playlist.trackCount
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