package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CropCategory
import com.example.data.model.PriceUnit
import com.example.localization.AppLanguage
import com.example.localization.LocalizationManager
import com.example.ui.theme.HarvestGreenContainer
import com.example.ui.theme.HarvestGreenPrimary
import com.example.ui.theme.SunGold
import com.example.ui.theme.SunGoldContainer

@Composable
fun KisanTopBar(
    selectedLanguage: AppLanguage,
    selectedUnit: PriceUnit,
    isAdminMode: Boolean,
    onOpenLanguageDialog: () -> Unit,
    onToggleUnit: () -> Unit,
    onToggleAdminMode: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = HarvestGreenPrimary,
        tonalElevation = 4.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Logo & Name
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.testTag("app_brand_header")
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🌾", fontSize = 24.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Kisan Price",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "किसान भाव • ಕಿಸಾನ್ ಬೆಲೆ",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFD6F5DC)
                        )
                    }
                }

                // Controls: Unit toggle, Language selector, Admin toggle
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Unit Toggle Button
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = SunGoldContainer,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { onToggleUnit() }
                            .testTag("unit_toggle_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.SwapHoriz,
                                contentDescription = "Switch Unit",
                                tint = SunGold,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = selectedUnit.label,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF78350F)
                            )
                        }
                    }

                    // Language button
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color.White.copy(alpha = 0.2f),
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { onOpenLanguageDialog() }
                            .testTag("language_selector_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = "Language",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = selectedLanguage.nativeName,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    // Admin button
                    IconButton(
                        onClick = onToggleAdminMode,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("admin_mode_toggle_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = "Admin Mode",
                            tint = if (isAdminMode) SunGold else Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            // Tagline
            Text(
                text = LocalizationManager.getTagline(selectedLanguage),
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFE8F5E9),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun LargeSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    language: AppLanguage,
    onVoiceClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hint = LocalizationManager.getText("search_hint", language)
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(
                text = hint,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = HarvestGreenPrimary
            )
        },
        trailingIcon = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { onQueryChange("") }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear search",
                            tint = Color.Gray
                        )
                    }
                }
                IconButton(
                    onClick = onVoiceClick,
                    modifier = Modifier.testTag("voice_search_button")
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(HarvestGreenContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Voice Search",
                            tint = HarvestGreenPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = HarvestGreenPrimary,
            unfocusedBorderColor = Color(0xFFD1D5DB)
        ),
        modifier = modifier
            .fillMaxWidth()
            .testTag("crop_search_text_field")
    )
}

@Composable
fun CategorySelectorRow(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val categories = listOf(
        CropCategory.ALL,
        CropCategory.VEGETABLES,
        CropCategory.FRUITS,
        CropCategory.GRAINS,
        CropCategory.PULSES
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { cat ->
            val isSelected = selectedCategory.equals(cat.id, ignoreCase = true)
            val localizedName = LocalizationManager.getCategoryName(cat.id, language)

            FilterChip(
                selected = isSelected,
                onClick = { onCategorySelected(cat.id) },
                label = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = cat.icon, fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = localizedName,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = HarvestGreenPrimary,
                    selectedLabelColor = Color.White,
                    containerColor = Color.White,
                    labelColor = Color(0xFF1F2937)
                ),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .height(44.dp)
                    .testTag("category_chip_${cat.id.lowercase()}")
            )
        }
    }
}

@Composable
fun LocationSelectorCard(
    selectedState: String?,
    selectedDistrict: String?,
    selectedMarket: String?,
    availableStates: List<String>,
    availableDistricts: List<String>,
    availableMarkets: List<String>,
    onSelectLocation: (String?, String?, String?) -> Unit,
    onClearLocation: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showStateMenu by remember { mutableStateOf(false) }
    var showDistrictMenu by remember { mutableStateOf(false) }
    var showMarketMenu by remember { mutableStateOf(false) }

    Surface(
        color = Color.White,
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 2.dp,
        shadowElevation = 1.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = HarvestGreenPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Location (State → District → Mandi):",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color(0xFF374151)
                    )
                }

                if (selectedState != null || selectedDistrict != null || selectedMarket != null) {
                    TextButton(onClick = onClearLocation) {
                        Text("Clear", color = Color(0xFFDC2626), fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // State Dropdown
                Box(modifier = Modifier.weight(1f)) {
                    OutlinedButton(
                        onClick = { showStateMenu = true },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().testTag("state_selector_button")
                    ) {
                        Text(
                            text = selectedState ?: "All States",
                            maxLines = 1,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    DropdownMenu(
                        expanded = showStateMenu,
                        onDismissRequest = { showStateMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("All States") },
                            onClick = {
                                onSelectLocation(null, null, null)
                                showStateMenu = false
                            }
                        )
                        availableStates.forEach { state ->
                            DropdownMenuItem(
                                text = { Text(state) },
                                onClick = {
                                    onSelectLocation(state, null, null)
                                    showStateMenu = false
                                }
                            )
                        }
                    }
                }

                // District Dropdown
                Box(modifier = Modifier.weight(1f)) {
                    OutlinedButton(
                        onClick = { showDistrictMenu = true },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().testTag("district_selector_button")
                    ) {
                        Text(
                            text = selectedDistrict ?: "All Districts",
                            maxLines = 1,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    DropdownMenu(
                        expanded = showDistrictMenu,
                        onDismissRequest = { showDistrictMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("All Districts") },
                            onClick = {
                                onSelectLocation(selectedState, null, null)
                                showDistrictMenu = false
                            }
                        )
                        availableDistricts.forEach { dist ->
                            DropdownMenuItem(
                                text = { Text(dist) },
                                onClick = {
                                    onSelectLocation(selectedState, dist, null)
                                    showDistrictMenu = false
                                }
                            )
                        }
                    }
                }

                // Market Dropdown
                Box(modifier = Modifier.weight(1.2f)) {
                    OutlinedButton(
                        onClick = { showMarketMenu = true },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().testTag("market_selector_button")
                    ) {
                        Text(
                            text = selectedMarket ?: "All Mandis",
                            maxLines = 1,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    DropdownMenu(
                        expanded = showMarketMenu,
                        onDismissRequest = { showMarketMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("All Mandis") },
                            onClick = {
                                onSelectLocation(selectedState, selectedDistrict, null)
                                showMarketMenu = false
                            }
                        )
                        availableMarkets.forEach { mandi ->
                            DropdownMenuItem(
                                text = { Text(mandi) },
                                onClick = {
                                    onSelectLocation(selectedState, selectedDistrict, mandi)
                                    showMarketMenu = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LanguageDialog(
    currentLanguage: AppLanguage,
    onSelectLanguage: (AppLanguage) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Select Language / भाषा चुनें",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(AppLanguage.ENGLISH, AppLanguage.HINDI, AppLanguage.TELUGU).forEach { lang ->
                    val isSelected = lang == currentLanguage
                    Surface(
                        color = if (isSelected) HarvestGreenContainer else Color(0xFFF3F4F6),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSelectLanguage(lang)
                                onDismiss()
                            }
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = lang.nativeName,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) HarvestGreenPrimary else Color(0xFF111827)
                                )
                                Text(
                                    text = lang.displayName,
                                    fontSize = 13.sp,
                                    color = Color(0xFF6B7280)
                                )
                            }
                            if (isSelected) {
                                Text(text = "✓", color = HarvestGreenPrimary, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = HarvestGreenPrimary)
            }
        }
    )
}
