package com.example.playlistmaker.data.favorites.impl

import android.util.Log
import com.example.playlistmaker.data.converters.TrackDbConverter
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.entity.TrackEntity
import com.example.playlistmaker.data.favorites.FavoritesRepository
import com.example.playlistmaker.data.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class FavoritesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConverter: TrackDbConverter
    ): FavoritesRepository {

    override fun getFavoritesTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDao().getTracks()
        emit(convertFromTrackEntity(tracks))
    }

    override suspend fun addToFavorites(track: Track): Flow<Boolean> = flow {
        Log.d("AAAA", "3")
        appDatabase.trackDao().insertTrack(convertToTrackEntity(track))
        emit(true)
    }

    override fun deleteFromFavorites(track: Track): Flow<Boolean> = flow {
        appDatabase.trackDao().deleteTrack(convertToTrackEntity(track))
        emit(true)
    }

    override fun isInFavorites(track: Track): Flow<Boolean> = flow {
        if (appDatabase.trackDao().isInFavoritesTracks(track.trackId).isEmpty()) {
            emit(false)
        } else {
            emit(true)
        }
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConverter.map(track) }
    }

    private fun convertToTrackEntity(track: Track): TrackEntity {
        return trackDbConverter.map(track)
    }

}