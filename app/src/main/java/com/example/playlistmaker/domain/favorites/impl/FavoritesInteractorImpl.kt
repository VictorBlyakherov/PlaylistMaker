package com.example.playlistmaker.domain.favorites.impl

import android.util.Log
import com.example.playlistmaker.data.favorites.FavoritesRepository
import com.example.playlistmaker.data.model.Track
import com.example.playlistmaker.domain.favorites.FavoritesInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoritesInteractorImpl(private val favoritesRepository: FavoritesRepository): FavoritesInteractor {

    override fun getFavoritesTracks(): Flow<List<Track>> {
        return favoritesRepository.getFavoritesTracks()
    }

    override suspend fun addToFavorites(track: Track): Flow<Boolean> {
        Log.d("AAAA", "1")
        return favoritesRepository.addToFavorites(track)
    }

    override fun deleteFromFavorites(track: Track): Flow<Boolean> {
        return favoritesRepository.deleteFromFavorites(track)
    }

    override fun isInFavorites(track: Track): Flow<Boolean> {
        return favoritesRepository.isInFavorites(track)
    }
}