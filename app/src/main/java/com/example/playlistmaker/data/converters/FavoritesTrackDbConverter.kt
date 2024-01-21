package com.example.playlistmaker.data.converters

import com.example.playlistmaker.data.db.entity.TrackFavoritesEntity
import com.example.playlistmaker.data.model.Track
import java.time.LocalDateTime
import java.time.ZoneOffset

class FavoritesTrackDbConverter {

    fun map(track: Track): TrackFavoritesEntity {
        return TrackFavoritesEntity(track.trackId, track.trackName, track.artistName, track.trackTimeMillis, track.artworkUrl100, track.collectionName,
            track.releaseDate, track.primaryGenreName, track.country, track.previewUrl, LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))
    }

    fun map(track: TrackFavoritesEntity): Track {
        return Track(track.trackId, track.trackName, track.artistName, track.trackTimeMillis, track.artworkUrl100, track.collectionName,
            track.releaseDate, track.primaryGenreName, track.country, track.previewUrl, true)
    }
}