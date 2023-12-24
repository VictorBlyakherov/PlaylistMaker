package com.example.playlistmaker.domain.favorites.impl

import com.example.playlistmaker.data.favorites.FavoritesRepository
import com.example.playlistmaker.data.model.Track
import com.example.playlistmaker.domain.favorites.FavoritesInteractor
import kotlinx.coroutines.flow.Flow

class FavoritesInteractorImpl(private val favoritesRepository: FavoritesRepository): FavoritesInteractor {

    override suspend fun getFavoritesTracks(): Flow<List<Track>> {
        return favoritesRepository.getFavoritesTracks()
    }

    override suspend fun addToFavorites(track: Track) {
        favoritesRepository.addToFavorites(track)
    }

    override suspend fun deleteFromFavorites(track: Track) {
        favoritesRepository.deleteFromFavorites(track)
    }

    override suspend fun getFavoriteTracksId(): Flow<List<Int>> {
        return favoritesRepository.getFavoriteTracksId()
    }

    override suspend fun fillMapForSearch(trackList: List<Track>, trackHistoryList: List<Track>) {
        favoritesRepository.fillMapForSearch(trackList, trackHistoryList)
    }

    override suspend fun fillMapForHistory(trackList: List<Track>) {
        favoritesRepository.fillMapForHistory(trackList)
    }

    override suspend fun getMap(): MutableMap<Int, Boolean> {
        return favoritesRepository.getMap()
    }

    override suspend fun addToMap(track: Track) {
        favoritesRepository.addToMap(track)
    }

    override suspend fun removeFromMap(track: Track) {
        favoritesRepository.removeFromMap(track)
    }

    override fun checkTrackForFavorites(track: Track): Boolean {
        return favoritesRepository.checkTrackForFavorites(track)
    }
}