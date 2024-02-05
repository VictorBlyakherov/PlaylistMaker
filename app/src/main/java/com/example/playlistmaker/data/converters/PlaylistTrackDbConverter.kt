package com.example.playlistmaker.data.converters

import com.example.playlistmaker.data.db.entity.TrackPlaylistEntity
import com.example.playlistmaker.data.model.Track


class PlaylistTrackDbConverter {

    fun map(track: Track): TrackPlaylistEntity {
        var vv = TrackPlaylistEntity(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl
        )


        return vv
    }

    fun map(track: TrackPlaylistEntity): Track {
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
            isInFavorites = false
        )
    }

}