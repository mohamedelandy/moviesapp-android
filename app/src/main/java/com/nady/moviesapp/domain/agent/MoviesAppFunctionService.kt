/**
 * File: MoviesAppFunctionService.kt
 * Brief: Part of the Movies App Showcase project.
 * It follows Clean Architecture, SOLID, and Result patterns.
 */

package com.nady.moviesapp.domain.agent

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.nady.moviesapp.data.datasource.MovieStaticDataSource
import com.nady.moviesapp.data.local.AppDatabase
import com.nady.moviesapp.data.repository.MovieRepositoryImpl

class MoviesAppFunctionService : Service() {

    private val binder = LocalBinder()
    private lateinit var appFunctions: MoviesAppFunctions

    inner class LocalBinder : Binder() {
        fun getService(): MoviesAppFunctionService = this@MoviesAppFunctionService
        fun getFunctions(): MoviesAppFunctions = appFunctions
    }

    override fun onCreate() {
        super.onCreate()
        val database = AppDatabase.getInstance(applicationContext)
        val repository = MovieRepositoryImpl(
            movieDao = database.movieDao()
        )
        appFunctions = MoviesAppFunctions(repository)
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }
}
