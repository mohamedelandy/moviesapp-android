/**
 * File: MySpaceViewModel.kt
 * Brief: Part of the Movies App Showcase project.
 * It follows Clean Architecture, SOLID, and Result patterns.
 */

package com.nady.moviesapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nady.moviesapp.domain.model.DownloadItem
import com.nady.moviesapp.domain.model.Resource
import com.nady.moviesapp.domain.model.Movie
import com.nady.moviesapp.domain.model.UserProfile
import com.nady.moviesapp.domain.repository.DownloadsRepository
import com.nady.moviesapp.domain.usecase.GetContinueWatchingUseCase
import com.nady.moviesapp.domain.usecase.GetSavedUniversesUseCase
import com.nady.moviesapp.domain.usecase.GetUserProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MySpaceViewModel(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getContinueWatchingUseCase: GetContinueWatchingUseCase,
    private val getSavedUniversesUseCase: GetSavedUniversesUseCase,
    private val downloadsRepository: DownloadsRepository
) : ViewModel() {

    val userProfile: StateFlow<UserProfile> = getUserProfileUseCase().mapNotNull { (it as? Resource.Success)?.data }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            UserProfile(avatarUrl = "")
        )

    val continueWatching: StateFlow<List<Movie>> = getContinueWatchingUseCase().mapNotNull { (it as? Resource.Success)?.data }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val savedUniverses: StateFlow<List<Movie>> = getSavedUniversesUseCase().mapNotNull { (it as? Resource.Success)?.data }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val downloads: StateFlow<List<DownloadItem>> = downloadsRepository.getDownloads().mapNotNull { (it as? Resource.Success)?.data }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val storageUsage: StateFlow<Pair<Float, Float>> = downloadsRepository.getStorageUsage().mapNotNull { (it as? Resource.Success)?.data }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Pair(18.4f, 256.0f))

    private val _selectedSavedFilter = MutableStateFlow("All Formats")
    val selectedSavedFilter: StateFlow<String> = _selectedSavedFilter.asStateFlow()

    private val _isVisionProMirrored = MutableStateFlow(false)
    val isVisionProMirrored: StateFlow<Boolean> = _isVisionProMirrored.asStateFlow()

    fun selectSavedFilter(filter: String) {
        _selectedSavedFilter.value = filter
    }

    fun toggleVisionProMirror() {
        _isVisionProMirrored.value = !_isVisionProMirrored.value
    }

    fun deleteDownload(id: String) {
        viewModelScope.launch {
            downloadsRepository.removeDownload(id)
        }
    }
}
