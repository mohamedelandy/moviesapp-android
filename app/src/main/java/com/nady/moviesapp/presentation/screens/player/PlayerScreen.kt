/**
 * File: PlayerScreen.kt
 * Brief: Part of the Movies App Showcase project.
 * It follows Clean Architecture, SOLID, and Result patterns.
 */

package com.nady.moviesapp.presentation.screens.player

import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cast
import androidx.compose.material.icons.filled.CastConnected
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.ui.PlayerView
import androidx.compose.ui.unit.sp
import com.nady.moviesapp.presentation.components.LocalAsyncImage
import com.nady.moviesapp.R
import com.nady.moviesapp.presentation.components.EqualizerWaveBar
import com.nady.moviesapp.presentation.viewmodel.PlayerViewModel
import com.nady.moviesapp.ui.theme.BrandAccent
import com.nady.moviesapp.ui.theme.BrandAccentGlow
import com.nady.moviesapp.ui.theme.SpatialCyan
import com.nady.moviesapp.ui.theme.SpatialPurple
import com.nady.moviesapp.ui.theme.spatialColors

@Composable
fun PlayerScreen(
    movieId: String,
    episodeNumber: Int,
    viewModel: PlayerViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var backProgress by remember { mutableStateOf(0f) }

    PredictiveBackHandler(enabled = true) { progressFlow ->
        try {
            progressFlow.collect { backEvent ->
                backProgress = backEvent.progress
            }
            onBack()
        } catch (e: java.util.concurrent.CancellationException) {
            backProgress = 0f
        }
    }

    LaunchedEffect(movieId, episodeNumber) {
        viewModel.setMovieAndEpisode(movieId, episodeNumber)
    }

    val movie by viewModel.movie.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val currentSeconds by viewModel.currentSeconds.collectAsState()
    val totalSeconds by viewModel.totalSeconds.collectAsState()
    val playbackSpeed by viewModel.playbackSpeed.collectAsState()
    val isHeadTrackingOn by viewModel.isHeadTrackingOn.collectAsState()
    val isLocked by viewModel.isLocked.collectAsState()
    val showXRay by viewModel.showXRay.collectAsState()
    val isCasting by viewModel.isCasting.collectAsState()
    val castDeviceName by viewModel.castDeviceName.collectAsState()
    val currentEpisode = viewModel.currentEpisodeNumber.collectAsState().value

    var areControlsVisible by remember { mutableStateOf(true) }
    val extendedColors = MaterialTheme.spatialColors

    val targetMovie = movie ?: return

    val progressFraction = (currentSeconds.toFloat() / totalSeconds.toFloat()).coerceIn(0f, 1f)

    val context = LocalContext.current
    val exoPlayer by viewModel.exoPlayer.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.initExoPlayer(context)
    }

    val scale = 1f - (backProgress * 0.12f)
    val alpha = 1f - (backProgress * 0.35f)
    val cornerRadius = (backProgress * 24).dp

    Box(
        modifier = modifier
            .fillMaxSize()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                this.alpha = alpha
                clip = backProgress > 0f
                shape = RoundedCornerShape(cornerRadius)
            }
            .background(Color.Black)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { areControlsVisible = !areControlsVisible }
            )
            .testTag("player_screen")
    ) {

        if (exoPlayer != null) {
            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        player = exoPlayer
                        useController = false
                        setShowBuffering(PlayerView.SHOW_BUFFERING_ALWAYS)
                    }
                },
                update = { playerView ->
                    if (playerView.player != exoPlayer) {
                        playerView.player = exoPlayer
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        } else {
            LocalAsyncImage(
                model = targetMovie.backdropUrl.ifEmpty { targetMovie.posterUrl },
                contentDescription = "Video Playback",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Black.copy(alpha = 0.85f),
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.92f)
                        )
                    )
                )
        )

        if (isCasting) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.88f))
                    .testTag("cast_active_overlay"),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(SpatialCyan.copy(alpha = 0.15f))
                            .border(1.5.dp, SpatialCyan, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CastConnected,
                            contentDescription = "Active Cast",
                            tint = SpatialCyan,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                    Text(
                        text = "STREAMING VIA GOOGLE CAST",
                        color = SpatialCyan,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        letterSpacing = 1.2.sp
                    )
                    Text(
                        text = castDeviceName ?: "Google Cast Receiver",
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp
                    )
                    EqualizerWaveBar(
                        barColor = SpatialCyan,
                        maxHeight = 16.dp,
                        isAnimating = isPlaying
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = areControlsVisible || isLocked,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(modifier = Modifier.fillMaxSize()) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, start = 16.dp, end = 16.dp)
                        .align(Alignment.TopCenter),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.6f))
                            .testTag("btn_player_back")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = targetMovie.title,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "S1:E$currentEpisode • The Resonance Fold",
                            color = Color.White.copy(alpha = 0.75f),
                            fontSize = 11.sp
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        IconButton(
                            onClick = {
                                try {
                                    val castContext = com.google.android.gms.cast.framework.CastContext.getSharedInstance(context)

                                } catch (e: Exception) {

                                }
                            },
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isCasting) SpatialCyan.copy(alpha = 0.25f) else Color.Black.copy(alpha = 0.6f)
                                )
                                .border(
                                    width = if (isCasting) 1.dp else 0.dp,
                                    color = if (isCasting) SpatialCyan else Color.Transparent,
                                    shape = CircleShape
                                )
                                .testTag("btn_player_cast")
                        ) {
                            Icon(
                                imageVector = if (isCasting) Icons.Default.CastConnected else Icons.Default.Cast,
                                contentDescription = "Google Cast",
                                tint = if (isCasting) SpatialCyan else Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        IconButton(
                            onClick = { viewModel.toggleLock() },
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isLocked) BrandAccent else Color.Black.copy(alpha = 0.6f)
                                )
                                .testTag("btn_player_lock")
                        ) {
                            Icon(
                                imageVector = if (isLocked) Icons.Default.Lock else Icons.Default.LockOpen,
                                contentDescription = "Lock",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                if (!isLocked) {

                    Row(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 76.dp)
                            .background(Color.Black.copy(alpha = 0.65f), RoundedCornerShape(20.dp))
                            .border(0.5.dp, SpatialCyan.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Headphones,
                            contentDescription = null,
                            tint = SpatialCyan,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "360° DOLBY ATMOS • HEAD-TRACKING ACTIVE",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                        EqualizerWaveBar(
                            barColor = SpatialCyan,
                            maxHeight = 12.dp,
                            isAnimating = isPlaying
                        )
                    }

                    Row(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalArrangement = Arrangement.spacedBy(40.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        IconButton(
                            onClick = { viewModel.seekRelative(-10) },
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.5f))
                                .testTag("btn_player_replay10")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Replay10,
                                contentDescription = "Replay 10s",
                                tint = Color.White,
                                modifier = Modifier.size(30.dp)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(BrandAccent)
                                .border(2.dp, BrandAccentGlow, CircleShape)
                                .clickable { viewModel.togglePlayPause() }
                                .testTag("btn_player_toggle"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isPlaying) "Pause" else "Play",
                                tint = Color.White,
                                modifier = Modifier.size(38.dp)
                            )
                        }

                        IconButton(
                            onClick = { viewModel.seekRelative(10) },
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.5f))
                                .testTag("btn_player_forward10")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Forward10,
                                contentDescription = "Forward 10s",
                                tint = Color.White,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 24.dp, start = 16.dp, end = 16.dp)
                    ) {

                        if (showXRay && targetMovie.cast.isNotEmpty()) {
                            val activeActor = targetMovie.cast.first()
                            Row(
                                modifier = Modifier
                                    .padding(bottom = 12.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.Black.copy(alpha = 0.75f))
                                    .border(0.5.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                LocalAsyncImage(
                                    model = activeActor.avatarUrl,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                )
                                Column {
                                    Text(
                                        text = "IN SCENE: ${activeActor.name.uppercase()}",
                                        color = SpatialCyan,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "as ${activeActor.characterName}",
                                        color = Color.White,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }

                        Slider(
                            value = progressFraction,
                            onValueChange = { viewModel.seekToFraction(it) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("player_scrubber"),
                            colors = SliderDefaults.colors(
                                thumbColor = BrandAccent,
                                activeTrackColor = BrandAccent,
                                inactiveTrackColor = Color.White.copy(alpha = 0.25f)
                            )
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = viewModel.formatTime(currentSeconds),
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            Box(
                                modifier = Modifier
                                    .background(BrandAccent.copy(alpha = 0.25f), RoundedCornerShape(6.dp))
                                    .border(0.5.dp, BrandAccent, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = viewModel.formatRemainingTime(),
                                    color = BrandAccentGlow,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }

                            Text(
                                text = viewModel.formatTime(totalSeconds),
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 12.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.White.copy(alpha = 0.15f))
                                    .clickable { viewModel.cyclePlaybackSpeed() }
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Speed,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "${playbackSpeed}x",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (isHeadTrackingOn) SpatialPurple.copy(alpha = 0.4f)
                                        else Color.White.copy(alpha = 0.15f)
                                    )
                                    .border(
                                        0.5.dp,
                                        if (isHeadTrackingOn) SpatialPurple else Color.Transparent,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable { viewModel.toggleHeadTracking() }
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Headphones,
                                    contentDescription = null,
                                    tint = if (isHeadTrackingOn) SpatialCyan else Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = if (isHeadTrackingOn) "Spatial 360°" else "Stereo",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            IconButton(
                                onClick = { viewModel.toggleXRay() },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.15f))
                            ) {
                                Icon(
                                    imageVector = if (showXRay) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = "X-Ray",
                                    tint = if (showXRay) SpatialCyan else Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(BrandAccent)
                                    .clickable { viewModel.playNextEpisode() }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                                    .testTag("btn_player_next_ep"),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.player_next_episode),
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Icon(
                                    imageVector = Icons.Default.SkipNext,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
