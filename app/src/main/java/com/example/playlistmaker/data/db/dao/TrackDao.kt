package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.TrackFavoritesEntity

@Dao
interface TrackDao {

    @Insert(entity = TrackFavoritesEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackFavoritesEntity)

    @Delete(entity = TrackFavoritesEntity::class)
    suspend fun deleteTrack(track: TrackFavoritesEntity)

    @Query("SELECT * FROM favorite_tracks_table order by timeAdded desc")
    suspend fun getTracks(): List<TrackFavoritesEntity>

    @Query("SELECT trackId FROM favorite_tracks_table")
    suspend fun getFavoriteTracksId(): List<Int>

}