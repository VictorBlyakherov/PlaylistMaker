package com.example.playlistmaker.data.favorites

import com.example.playlistmaker.data.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {

    fun getFavoritesTracks(): Flow<List<Track>>

    suspend fun addToFavorites(track: Track): Flow<Boolean>

    fun deleteFromFavorites(track: Track): Flow<Boolean>

    fun isInFavorites(track: Track): Flow<Boolean>

}