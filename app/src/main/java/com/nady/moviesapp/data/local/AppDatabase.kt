/**
 * File: AppDatabase.kt
 * Brief: Part of the Movies App Showcase project.
 * It follows Clean Architecture, SOLID, and Result patterns.
 */

package com.nady.moviesapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nady.moviesapp.data.local.entities.AppSettingEntity
import com.nady.moviesapp.data.local.entities.DownloadEntity
import com.nady.moviesapp.data.local.entities.PlaybackProgressEntity
import com.nady.moviesapp.data.local.entities.WatchlistEntity

@Database(
    entities = [
        WatchlistEntity::class,
        PlaybackProgressEntity::class,
        DownloadEntity::class,
        AppSettingEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "movies_app_showcase.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
