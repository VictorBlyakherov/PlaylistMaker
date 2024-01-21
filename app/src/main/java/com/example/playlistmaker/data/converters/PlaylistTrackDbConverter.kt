package com.example.playlistmaker.data.converters

import com.example.playlistmaker.data.db.entity.TrackPlaylistEntity
import com.example.playlistmaker.data.model.Track


class PlaylistTrackDbConverter {

    fun map(track: Track): TrackPlaylistEntity {
        return TrackPlaylistEntity(track.trackId, track.trackName, track.artistName, track.trackTimeMillis, track.artworkUrl100, track.collectionName,
            track.releaseDate, track.primaryGenreName, track.country, track.previewUrl)
    }

    fun map(track: TrackPlaylistEntity): Track {
        return Track(track.trackId, track.trackName, track.artistName, track.trackTimeMillis, track.artworkUrl100, track.collectionName,
            track.releaseDate, track.primaryGenreName, track.country, track.previewUrl, false)
    }

}