/**
 * File: DownloadsRepositoryImpl.kt
 * Brief: Part of the Movies App Showcase project.
 * It follows Clean Architecture, SOLID, and Result patterns.
 */

package com.nady.moviesapp.data.repository

import com.nady.moviesapp.data.datasource.MovieStaticDataSource
import com.nady.moviesapp.data.local.MovieDao
import com.nady.moviesapp.data.local.entities.DownloadEntity
import com.nady.moviesapp.domain.model.DownloadItem
import com.nady.moviesapp.domain.model.Resource
import com.nady.moviesapp.domain.repository.DownloadsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.catch

class DownloadsRepositoryImpl(
    private val movieDao: MovieDao
) : DownloadsRepository {

    override fun getDownloads(): Flow<Resource<List<DownloadItem>>> {
        return movieDao.getAllDownloads().map { entities ->
            val list = if (entities.isNotEmpty()) {
                entities.map { entity ->
                    DownloadItem(
                        id = entity.id,
                        movieId = entity.movieId,
                        title = entity.title,
                        subtitle = entity.subtitle,
                        formatBadge = entity.formatBadge,
                        fileSizeText = entity.fileSizeText,
                        posterUrl = entity.posterUrl,
                        expiryText = entity.expiryText
                    )
                }
            } else {
                MovieStaticDataSource.initialDownloads
            }
            Resource.Success(list) as Resource<List<DownloadItem>>
        }.catch { emit(Resource.Error(it.message ?: "Error", it)) }
    }

    override suspend fun addDownload(item: DownloadItem): Resource<Unit> {
        return try {
            movieDao.insertDownload(
                DownloadEntity(
                    id = item.id,
                    movieId = item.movieId,
                    title = item.title,
                    subtitle = item.subtitle,
                    formatBadge = item.formatBadge,
                    fileSizeText = item.fileSizeText,
                    posterUrl = item.posterUrl,
                    expiryText = item.expiryText
                )
            )
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error", e)
        }
    }

    override suspend fun removeDownload(id: String): Resource<Unit> {
        return try {
            movieDao.deleteDownload(id)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error", e)
        }
    }

    override fun getStorageUsage(): Flow<Resource<Pair<Float, Float>>> = flow {
        emit(Resource.Success(Pair(18.4f, 256.0f)) as Resource<Pair<Float, Float>>)
    }.catch { emit(Resource.Error(it.message ?: "Error", it)) }
}
