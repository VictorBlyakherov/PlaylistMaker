package com.example.playlistmaker.data.favorites

import com.example.playlistmaker.data.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {

    var favoritesMap: MutableMap<Int, Boolean>

    suspend fun fillMapForSearch(trackList: List<Track>, trackHistoryList: List<Track>)

    suspend fun fillMapForHistory(trackList: List<Track>)

    suspend fun getMap(): MutableMap<Int, Boolean>

    suspend fun addToMap(track: Track)

    suspend fun removeFromMap(track: Track)

    fun checkTrackForFavorites(track: Track): Boolean

    suspend fun getFavoritesTracks(): Flow<List<Track>>

    suspend fun addToFavorites(track: Track)

    suspend fun deleteFromFavorites(track: Track)

    suspend fun getFavoriteTracksId(): Flow<List<Int>>

}