/**
 * File: LiquidBottomDock.kt
 * Brief: Part of the Movies App Showcase project.
 * It follows Clean Architecture, SOLID, and Result patterns.
 */

package com.nady.moviesapp.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Headphones
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nady.moviesapp.R
import com.nady.moviesapp.data.datasource.MovieStaticDataSource
import com.nady.moviesapp.presentation.navigation.Screen
import com.nady.moviesapp.ui.theme.BrandAccent
import com.nady.moviesapp.ui.theme.BrandAccentGlow
import com.nady.moviesapp.ui.theme.spatialColors

sealed class NavItem(
    val route: String,
    val filledIcon: ImageVector,
    val outlinedIcon: ImageVector,
    val titleResId: Int,
    val testTag: String
) {
    data object Home : NavItem(Screen.Home.route, Icons.Filled.Home, Icons.Outlined.Home, R.string.nav_home, "nav_home")
    data object Explore : NavItem(Screen.Explore.route, Icons.Filled.Search, Icons.Outlined.Search, R.string.nav_explore, "nav_explore")
    data object Spatial : NavItem(Screen.SpatialLounge.route, Icons.Filled.Headphones, Icons.Outlined.Headphones, R.string.nav_spatial, "nav_spatial")
    data object MySpace : NavItem(Screen.MySpace.route, Icons.Filled.Person, Icons.Outlined.Person, R.string.nav_myspace, "nav_myspace")
}

@Composable
fun LiquidBottomDock(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(NavItem.Home, NavItem.Explore, NavItem.Spatial, NavItem.MySpace)
    val extendedColors = MaterialTheme.spatialColors

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .shadow(elevation = 20.dp, shape = RoundedCornerShape(32.dp), spotColor = Color.Black.copy(alpha = 0.5f))
                .clip(RoundedCornerShape(32.dp))
                .background(
                    if (MaterialTheme.colorScheme.surface == Color(0xFF131313)) {
                        Color(0xE61A1919) 
                    } else {
                        Color(0xF0FFFFFF) 
                    }
                )
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(
                        listOf(
                            extendedColors.glassHighlight,
                            extendedColors.glassBorder
                        )
                    ),
                    shape = RoundedCornerShape(32.dp)
                )
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEach { item ->
                    val isSelected = currentRoute == item.route

                    val iconTint by animateColorAsState(
                        targetValue = if (isSelected) BrandAccent else extendedColors.textSecondary,
                        label = "iconTint"
                    )

                    val indicatorScale by animateDpAsState(
                        targetValue = if (isSelected) 44.dp else 40.dp,
                        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                        label = "indicatorScale"
                    )

                    Box(
                        modifier = Modifier
                            .size(indicatorScale)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) BrandAccent.copy(alpha = 0.12f) else Color.Transparent
                            )
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = { onNavigate(item.route) }
                            )
                            .testTag(item.testTag),
                        contentAlignment = Alignment.Center
                    ) {
                        if (item is NavItem.MySpace && isSelected) {

                            LocalAsyncImage(
                                model = MovieStaticDataSource.AVATAR_ALEX_ALT,
                                contentDescription = stringResource(item.titleResId),
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .border(1.5.dp, BrandAccent, CircleShape)
                            )
                        } else {
                            Icon(
                                imageVector = if (isSelected) item.filledIcon else item.outlinedIcon,
                                contentDescription = stringResource(item.titleResId),
                                tint = iconTint,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        if (isSelected) {

                            Box(
                                modifier = Modifier
                                    .size(4.dp)
                                    .align(Alignment.BottomCenter)
                                    .offset(y = (-4).dp)
                                    .background(BrandAccentGlow, CircleShape)
                            )
                        }
                    }
                }
            }
        }
    }
}
