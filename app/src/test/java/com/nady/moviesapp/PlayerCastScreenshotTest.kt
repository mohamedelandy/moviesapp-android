package com.nady.moviesapp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cast
import androidx.compose.material.icons.filled.CastConnected
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nady.moviesapp.domain.model.AppThemeMode
import com.nady.moviesapp.presentation.components.EqualizerWaveBar
import com.nady.moviesapp.ui.theme.MoviesAppTheme
import com.nady.moviesapp.ui.theme.BrandAccent
import com.nady.moviesapp.ui.theme.SpatialCyan
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [34])
class PlayerCastScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun player_active_cast_overlay_screenshot() {
        composeTestRule.setContent {
            MoviesAppTheme(themeMode = AppThemeMode.DARK) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    // Active Google Cast Overlay
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
                            text = "Living Room TV • Bravia 4K",
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp
                        )
                        EqualizerWaveBar(
                            barColor = SpatialCyan,
                            maxHeight = 16.dp,
                            isAnimating = true
                        )
                    }

                    // Top Bar with Cast & Lock buttons
                    Row(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = {},
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(SpatialCyan.copy(alpha = 0.25f))
                                .border(1.dp, SpatialCyan, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CastConnected,
                                contentDescription = "Google Cast",
                                tint = SpatialCyan,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        IconButton(
                            onClick = {},
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.6f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.LockOpen,
                                contentDescription = "Lock",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }

        composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/player_active_cast.png")
    }
}
