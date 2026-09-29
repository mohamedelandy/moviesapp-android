/**
 * File: PlayerViewModel.kt
 * Brief: Part of the Movies App Showcase project.
 * It follows Clean Architecture, SOLID, and Result patterns.
 */

package com.nady.moviesapp.presentation.viewmodel

import android.content.Context
import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.nady.moviesapp.domain.model.Movie
import com.nady.moviesapp.domain.model.Resource
import com.nady.moviesapp.domain.usecase.GetMovieDetailsUseCase
import com.nady.moviesapp.domain.usecase.UpdatePlaybackProgressUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val updatePlaybackProgressUseCase: UpdatePlaybackProgressUseCase
) : ViewModel() {

    private val _movieId = MutableStateFlow("solaris_echo")
    private val _episodeNumber = MutableStateFlow(1)

    val currentEpisodeNumber: StateFlow<Int> = _episodeNumber.asStateFlow()

    @kotlin.OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val movie: StateFlow<Movie?> = _movieId.flatMapLatest { id ->
        getMovieDetailsUseCase(id)
    }.map { (it as? Resource.Success)?.data }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _isPlaying = MutableStateFlow(true)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentSeconds = MutableStateFlow(2294L)
    val currentSeconds: StateFlow<Long> = _currentSeconds.asStateFlow()

    private val _totalSeconds = MutableStateFlow(3260L)
    val totalSeconds: StateFlow<Long> = _totalSeconds.asStateFlow()

    private val _playbackSpeed = MutableStateFlow(1.0f)
    val playbackSpeed: StateFlow<Float> = _playbackSpeed.asStateFlow()

    private val _isHeadTrackingOn = MutableStateFlow(true)
    val isHeadTrackingOn: StateFlow<Boolean> = _isHeadTrackingOn.asStateFlow()

    private val _isLocked = MutableStateFlow(false)
    val isLocked: StateFlow<Boolean> = _isLocked.asStateFlow()

    private val _showXRay = MutableStateFlow(true)
    val showXRay: StateFlow<Boolean> = _showXRay.asStateFlow()

    private val _exoPlayer = MutableStateFlow<ExoPlayer?>(null)
    val exoPlayer: StateFlow<ExoPlayer?> = _exoPlayer.asStateFlow()

    private val _isCasting = MutableStateFlow(false)
    val isCasting: StateFlow<Boolean> = _isCasting.asStateFlow()

    private val _castDeviceName = MutableStateFlow<String?>(null)
    val castDeviceName: StateFlow<String?> = _castDeviceName.asStateFlow()

    private var castPlayer: androidx.media3.cast.CastPlayer? = null

    private var playbackJob: Job? = null

    private val defaultVideoUri = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"

    fun initExoPlayer(context: Context) {
        if (_exoPlayer.value != null) return

        val player = ExoPlayer.Builder(context.applicationContext).build().apply {
            val mediaItem = MediaItem.fromUri(defaultVideoUri)
            setMediaItem(mediaItem)
            repeatMode = Player.REPEAT_MODE_ONE
            playWhenReady = _isPlaying.value
            playbackParameters = PlaybackParameters(_playbackSpeed.value)
            prepare()
            seekTo(_currentSeconds.value * 1000L)

            addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlayingNow: Boolean) {
                    _isPlaying.value = isPlayingNow
                }

                override fun onPlaybackStateChanged(playbackState: Int) {
                    if (playbackState == Player.STATE_READY && duration > 0) {
                        _totalSeconds.value = duration / 1000L
                    }
                }
            })
        }
        _exoPlayer.value = player
        initCast(context)
    }

    fun initCast(context: Context) {
        if (castPlayer != null) return
        try {
            val castContext = com.google.android.gms.cast.framework.CastContext.getSharedInstance(context.applicationContext)
            val player = androidx.media3.cast.CastPlayer(castContext).apply {
                setSessionAvailabilityListener(object : androidx.media3.cast.SessionAvailabilityListener {
                    override fun onCastSessionAvailable() {
                        _isCasting.value = true
                        _castDeviceName.value = castContext.sessionManager.currentCastSession?.castDevice?.friendlyName ?: "Chromecast"
                        val mediaItem = MediaItem.fromUri(defaultVideoUri)
                        setMediaItem(mediaItem)
                        prepare()
                        seekTo(_currentSeconds.value * 1000L)
                        if (_isPlaying.value) play()
                        _exoPlayer.value?.pause()
                    }

                    override fun onCastSessionUnavailable() {
                        _isCasting.value = false
                        _castDeviceName.value = null
                        _exoPlayer.value?.let {
                            it.seekTo(_currentSeconds.value * 1000L)
                            if (_isPlaying.value) it.play()
                        }
                    }
                })
            }
            castPlayer = player
        } catch (e: Exception) {

        }
    }

    init {
        startPlaybackTimer()
    }

    private fun startPlaybackTimer() {
        playbackJob?.cancel()
        playbackJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                if (_isPlaying.value && !_isLocked.value) {
                    val next = _currentSeconds.value + 1
                    if (next <= _totalSeconds.value) {
                        _currentSeconds.value = next
                    } else {
                        _isPlaying.value = false
                    }
                }
            }
        }
    }

    fun setMovieAndEpisode(movieId: String, episode: Int) {
        _movieId.value = movieId
        _episodeNumber.value = episode
    }

    fun togglePlayPause() {
        if (_isLocked.value) return
        val newPlaying = !_isPlaying.value
        _isPlaying.value = newPlaying
        if (_isCasting.value) {
            castPlayer?.let {
                if (newPlaying) it.play() else it.pause()
            }
        } else {
            _exoPlayer.value?.let {
                if (newPlaying) it.play() else it.pause()
            }
        }
    }

    fun seekRelative(secondsDelta: Long) {
        if (_isLocked.value) return
        val newSeconds = (_currentSeconds.value + secondsDelta).coerceIn(0L, _totalSeconds.value)
        _currentSeconds.value = newSeconds
        if (_isCasting.value) {
            castPlayer?.seekTo(newSeconds * 1000L)
        } else {
            _exoPlayer.value?.seekTo(newSeconds * 1000L)
        }
    }

    fun seekToFraction(fraction: Float) {
        if (_isLocked.value) return
        val newSeconds = (fraction.coerceIn(0f, 1f) * _totalSeconds.value).toLong()
        _currentSeconds.value = newSeconds
        if (_isCasting.value) {
            castPlayer?.seekTo(newSeconds * 1000L)
        } else {
            _exoPlayer.value?.seekTo(newSeconds * 1000L)
        }
    }

    fun cyclePlaybackSpeed() {
        if (_isLocked.value) return
        val speeds = listOf(1.0f, 1.25f, 1.5f, 2.0f)
        val currentIndex = speeds.indexOf(_playbackSpeed.value)
        val newSpeed = speeds[(currentIndex + 1) % speeds.size]
        _playbackSpeed.value = newSpeed
        _exoPlayer.value?.playbackParameters = PlaybackParameters(newSpeed)
    }

    fun toggleHeadTracking() {
        if (_isLocked.value) return
        _isHeadTrackingOn.value = !_isHeadTrackingOn.value
    }

    fun toggleLock() {
        _isLocked.value = !_isLocked.value
    }

    fun toggleXRay() {
        _showXRay.value = !_showXRay.value
    }

    fun playNextEpisode() {
        val next = _episodeNumber.value + 1
        _episodeNumber.value = next
        _currentSeconds.value = 0L
        _isPlaying.value = true
        _exoPlayer.value?.let {
            it.seekTo(0L)
            it.play()
        }
    }

    fun formatTime(seconds: Long): String {
        val mins = seconds / 60
        val secs = seconds % 60
        return String.format("%02d:%02d", mins, secs)
    }

    fun formatRemainingTime(): String {
        val remaining = (_totalSeconds.value - _currentSeconds.value).coerceAtLeast(0L)
        val mins = remaining / 60
        val secs = remaining % 60
        return String.format("%02d:%02d REMAINING", mins, secs)
    }

    override fun onCleared() {
        super.onCleared()
        playbackJob?.cancel()
        _exoPlayer.value?.release()
        _exoPlayer.value = null
        castPlayer?.release()
        castPlayer = null
        viewModelScope.launch {
            val progress = _currentSeconds.value.toFloat() / _totalSeconds.value.toFloat()
            updatePlaybackProgressUseCase(
                movieId = _movieId.value,
                episodeNumber = _episodeNumber.value,
                progress = progress,
                timestamp = formatTime(_currentSeconds.value)
            )
        }
    }
}
