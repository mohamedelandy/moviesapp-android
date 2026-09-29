/**
 * File: MovieEntities.kt
 * Brief: Part of the Movies App Showcase project.
 * It follows Clean Architecture, SOLID, and Result patterns.
 */

package com.nady.moviesapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watchlist")
data class WatchlistEntity(
    @PrimaryKey val movieId: String,
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "playback_progress")
data class PlaybackProgressEntity(
    @PrimaryKey val movieId: String,
    val episodeNumber: Int = 1,
    val progress: Float = 0f,
    val timestamp: String = "00:00",
    val lastWatchedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "downloads")
data class DownloadEntity(
    @PrimaryKey val id: String,
    val movieId: String,
    val title: String,
    val subtitle: String,
    val formatBadge: String,
    val fileSizeText: String,
    val posterUrl: String,
    val expiryText: String,
    val downloadedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "app_settings")
data class AppSettingEntity(
    @PrimaryKey val key: String,
    val value: String
)
