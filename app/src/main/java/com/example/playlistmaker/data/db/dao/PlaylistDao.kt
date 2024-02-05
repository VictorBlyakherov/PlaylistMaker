package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.data.db.entity.TrackPlaylistEntity

@Dao
interface PlaylistDao {

    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Delete(entity = PlaylistEntity::class)
    suspend fun deletePlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlist_table order by playlistId desc")
    suspend fun getPlaylists(): List<PlaylistEntity>

    @Update(entity = PlaylistEntity::class)
    suspend fun updatePlaylist(playlist: PlaylistEntity)

    @Insert(entity = TrackPlaylistEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrackToPlaylist(trackPlaylistEntity: TrackPlaylistEntity)

    @Delete(entity = TrackPlaylistEntity::class,)
    suspend fun deleteTrackFromPlaylist(trackPlaylistEntity: TrackPlaylistEntity)

    @Query("SELECT * FROM playlist_tracks_table")
    suspend fun getAllTracks(): List<TrackPlaylistEntity>

}