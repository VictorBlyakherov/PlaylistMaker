package com.example.playlistmaker.data.favorites.impl

import android.util.Log
import com.example.playlistmaker.data.converters.TrackDbConverter
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.entity.TrackEntity
import com.example.playlistmaker.data.favorites.FavoritesRepository
import com.example.playlistmaker.data.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList


class FavoritesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConverter: TrackDbConverter
    ): FavoritesRepository {

    override var favoritesMap: MutableMap<Int, Boolean> = mutableMapOf()
    override suspend fun fillMapForSearch(trackList: List<Track>, trackHistoryList: List<Track>) {
        favoritesMap.clear()
        fillMapForHistory(trackHistoryList)
        val trackIdList = getFavoriteTracksId().flatMapConcat { it.asFlow() }.toList()
        fillMap(trackIdList, trackList)
    }

    private fun fillMap(idList: List<Int>, trackList: List<Track>) {
        var tempStatus: Boolean
        for (track in trackList) {
            tempStatus = idList.filter { it == track.trackId }.size == 1
            favoritesMap[track.trackId] = tempStatus
        }
    }

    override suspend fun fillMapForHistory(trackList: List<Track>) {
        val trackIdList = getFavoriteTracksId().flatMapConcat { it.asFlow() }.toList()
        fillMap(trackIdList, trackList)
    }

    override suspend fun getMap(): MutableMap<Int, Boolean> {
        return favoritesMap
    }

    override suspend fun addToMap(track: Track) {
        favoritesMap[track.trackId] = true
    }

    override suspend fun removeFromMap(track: Track) {
        favoritesMap[track.trackId] = false
    }

    override fun checkTrackForFavorites(track: Track): Boolean {
        return favoritesMap.getOrDefault(track.trackId, false)
    }


    override suspend fun getFavoritesTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDao().getTracks()

        emit(convertFromTrackEntity(tracks))
    }

    override suspend fun addToFavorites(track: Track) {
        appDatabase.trackDao().insertTrack(convertToTrackEntity(track))
    }

    override suspend fun deleteFromFavorites(track: Track) {
        appDatabase.trackDao().deleteTrack(convertToTrackEntity(track))
    }

    override suspend fun getFavoriteTracksId(): Flow<List<Int>> = flow {
        val idList = appDatabase.trackDao().getFavoriteTracksId()
        emit(idList)
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConverter.map(track) }
    }

    private fun convertToTrackEntity(track: Track): TrackEntity {
        return trackDbConverter.map(track)
    }

}