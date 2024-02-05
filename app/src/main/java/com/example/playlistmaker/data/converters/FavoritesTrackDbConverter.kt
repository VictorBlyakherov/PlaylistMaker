package com.example.playlistmaker.data.converters

import com.example.playlistmaker.data.db.entity.TrackFavoritesEntity
import com.example.playlistmaker.data.model.Track
import java.time.LocalDateTime
import java.time.ZoneOffset

class FavoritesTrackDbConverter {

    fun map(track: Track): TrackFavoritesEntity {
        return TrackFavoritesEntity(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl,
            timeAdded = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
        )
    }

    fun map(track: TrackFavoritesEntity): Track {
        return Track(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl,
            isInFavorites = true
        )
    }
}