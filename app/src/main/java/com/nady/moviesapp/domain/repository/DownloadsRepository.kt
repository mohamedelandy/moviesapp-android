/**
 * File: DownloadsRepository.kt
 * Brief: Part of the Movies App Showcase project.
 * It follows Clean Architecture, SOLID, and Result patterns.
 */

package com.nady.moviesapp.domain.repository

import com.nady.moviesapp.domain.model.DownloadItem
import com.nady.moviesapp.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface DownloadsRepository {
    fun getDownloads(): Flow<Resource<List<DownloadItem>>>
    suspend fun addDownload(item: DownloadItem): Resource<Unit>
    suspend fun removeDownload(id: String): Resource<Unit>
    fun getStorageUsage(): Flow<Resource<Pair<Float, Float>>>
}
