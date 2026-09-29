/**
 * File: LanguageThemeDialog.kt
 * Brief: Part of the Movies App Showcase project.
 * It follows Clean Architecture, SOLID, and Result patterns.
 */

package com.nady.moviesapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.SettingsBrightness
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nady.moviesapp.R
import com.nady.moviesapp.domain.model.AppThemeMode
import com.nady.moviesapp.domain.model.SupportedLanguage
import com.nady.moviesapp.ui.theme.BrandAccent
import com.nady.moviesapp.ui.theme.spatialColors

@Composable
fun LanguageThemeDialog(
    currentTheme: AppThemeMode,
    currentLanguage: SupportedLanguage,
    onSelectTheme: (AppThemeMode) -> Unit,
    onSelectLanguage: (SupportedLanguage) -> Unit,
    onDismiss: () -> Unit
) {
    val extendedColors = MaterialTheme.spatialColors
    var selectedTab by remember { mutableStateOf(0) } 

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("dialog_language_theme"),
        containerColor = MaterialTheme.colorScheme.surface,
        title = {
            Column {
                Text(
                    text = stringResource(R.string.quick_settings_title),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (selectedTab == 0) BrandAccent else Color.Transparent)
                            .clickable { selectedTab = 0 }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = null,
                                tint = if (selectedTab == 0) Color.White else extendedColors.textSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = stringResource(R.string.tab_language),
                                color = if (selectedTab == 0) Color.White else extendedColors.textSecondary,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (selectedTab == 1) BrandAccent else Color.Transparent)
                            .clickable { selectedTab = 1 }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.SettingsBrightness,
                                contentDescription = null,
                                tint = if (selectedTab == 1) Color.White else extendedColors.textSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = stringResource(R.string.tab_theme),
                                color = if (selectedTab == 1) Color.White else extendedColors.textSecondary,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        },
        text = {
            Column(modifier = Modifier.height(280.dp)) {
                if (selectedTab == 0) {
                    Text(
                        text = stringResource(R.string.choose_language_desc),
                        style = MaterialTheme.typography.bodySmall,
                        color = extendedColors.textMuted,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(SupportedLanguage.entries) { lang ->
                            val isSelected = lang == currentLanguage
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (isSelected) BrandAccent.copy(alpha = 0.15f)
                                        else MaterialTheme.colorScheme.surfaceVariant
                                    )
                                    .border(
                                        1.dp,
                                        if (isSelected) BrandAccent else Color.Transparent,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable { onSelectLanguage(lang) }
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = lang.nativeName,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "${lang.displayName} • ${if (lang.isRtl) "RTL" else "LTR"}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = extendedColors.textSecondary
                                    )
                                }

                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = BrandAccent,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                } else {
                    Text(
                        text = stringResource(R.string.choose_theme_desc),
                        style = MaterialTheme.typography.bodySmall,
                        color = extendedColors.textMuted,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    val themes = listOf(
                        Triple(AppThemeMode.DARK, stringResource(R.string.theme_dark), Icons.Default.DarkMode),
                        Triple(AppThemeMode.LIGHT, stringResource(R.string.theme_light), Icons.Default.LightMode),
                        Triple(AppThemeMode.SYSTEM, stringResource(R.string.theme_system), Icons.Default.SettingsBrightness)
                    )

                    themes.forEach { (mode, label, icon) ->
                        val isSelected = mode == currentTheme
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    if (isSelected) BrandAccent.copy(alpha = 0.15f)
                                    else MaterialTheme.colorScheme.surfaceVariant
                                )
                                .border(
                                    1.dp,
                                    if (isSelected) BrandAccent else Color.Transparent,
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable { onSelectTheme(mode) }
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    tint = if (isSelected) BrandAccent else extendedColors.textSecondary,
                                    modifier = Modifier.size(22.dp)
                                )
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = BrandAccent,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = BrandAccent)
            ) {
                Text(stringResource(R.string.btn_done), color = Color.White)
            }
        }
    )
}
