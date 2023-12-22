package com.example.playlistmaker.domain.favorites

import com.example.playlistmaker.data.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {

    fun getFavoritesTracks(): Flow<List<Track>>

    suspend fun addToFavorites(track: Track): Flow<Boolean>

    fun deleteFromFavorites(track: Track): Flow<Boolean>

    fun isInFavorites(track: Track): Flow<Boolean>

}