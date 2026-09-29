/**
 * File: MovieDao.kt
 * Brief: Part of the Movies App Showcase project.
 * It follows Clean Architecture, SOLID, and Result patterns.
 */

package com.nady.moviesapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nady.moviesapp.data.local.entities.AppSettingEntity
import com.nady.moviesapp.data.local.entities.DownloadEntity
import com.nady.moviesapp.data.local.entities.PlaybackProgressEntity
import com.nady.moviesapp.data.local.entities.WatchlistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Query("SELECT * FROM watchlist ORDER BY addedAt DESC")
    fun getAllWatchlist(): Flow<List<WatchlistEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM watchlist WHERE movieId = :movieId)")
    fun isMovieInWatchlist(movieId: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWatchlist(entity: WatchlistEntity)

    @Query("DELETE FROM watchlist WHERE movieId = :movieId")
    suspend fun deleteFromWatchlist(movieId: String)

    @Query("SELECT * FROM playback_progress ORDER BY lastWatchedAt DESC")
    fun getAllPlaybackProgress(): Flow<List<PlaybackProgressEntity>>

    @Query("SELECT * FROM playback_progress WHERE movieId = :movieId LIMIT 1")
    fun getPlaybackProgress(movieId: String): Flow<PlaybackProgressEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePlaybackProgress(progress: PlaybackProgressEntity)

    @Query("SELECT * FROM downloads ORDER BY downloadedAt DESC")
    fun getAllDownloads(): Flow<List<DownloadEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDownload(download: DownloadEntity)

    @Query("DELETE FROM downloads WHERE id = :id")
    suspend fun deleteDownload(id: String)

    @Query("SELECT * FROM app_settings WHERE `key` = :key LIMIT 1")
    fun getSetting(key: String): Flow<AppSettingEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSetting(setting: AppSettingEntity)
}
