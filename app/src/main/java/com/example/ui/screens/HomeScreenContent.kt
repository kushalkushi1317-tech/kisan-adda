package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CropPrice
import com.example.data.model.PriceUnit
import com.example.localization.AppLanguage
import com.example.localization.LocalizationManager
import com.example.ui.components.CategorySelectorRow
import com.example.ui.components.CropPriceCard
import com.example.ui.components.LargeSearchBar
import com.example.ui.components.LocationSelectorCard
import com.example.ui.theme.HarvestGreenContainer
import com.example.ui.theme.HarvestGreenPrimary
import com.example.ui.theme.PriceDownBackground
import com.example.ui.theme.PriceDownRed
import com.example.ui.theme.PriceUpBackground
import com.example.ui.theme.PriceUpGreen
import com.example.ui.theme.SunGold
import com.example.ui.theme.SunGoldContainer

@Composable
fun HomeScreenContent(
    query: String,
    onQueryChange: (String) -> Unit,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    selectedState: String?,
    selectedDistrict: String?,
    selectedMarket: String?,
    availableStates: List<String>,
    availableDistricts: List<String>,
    availableMarkets: List<String>,
    onSelectLocation: (String?, String?, String?) -> Unit,
    onClearLocation: () -> Unit,
    cropPrices: List<CropPrice>,
    favoriteCropIds: Set<Long>,
    unit: PriceUnit,
    language: AppLanguage,
    onToggleFavorite: (CropPrice) -> Unit,
    onShareCrop: (CropPrice) -> Unit,
    onCompareCrop: (CropPrice) -> Unit,
    onViewTrends: (CropPrice) -> Unit,
    onVoiceClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val topPrices = cropPrices.sortedByDescending { it.avgPrice }.take(5)
    val gainers = cropPrices.filter { it.priceChangePercent > 0 }.sortedByDescending { it.priceChangePercent }.take(3)
    val losers = cropPrices.filter { it.priceChangePercent < 0 }.sortedBy { it.priceChangePercent }.take(3)

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Disclaimer banner (Important user prompt requirement)
        item {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = SunGoldContainer.copy(alpha = 0.6f),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Sample Data Notice",
                        tint = SunGold,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = LocalizationManager.getText("sample_data_banner", language),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF78350F)
                    )
                }
            }
        }

        // Search bar
        item {
            LargeSearchBar(
                query = query,
                onQueryChange = onQueryChange,
                language = language,
                onVoiceClick = onVoiceClick
            )
        }

        // Location Selector Card
        item {
            LocationSelectorCard(
                selectedState = selectedState,
                selectedDistrict = selectedDistrict,
                selectedMarket = selectedMarket,
                availableStates = availableStates,
                availableDistricts = availableDistricts,
                availableMarkets = availableMarkets,
                onSelectLocation = onSelectLocation,
                onClearLocation = onClearLocation
            )
        }

        // Category Chips
        item {
            CategorySelectorRow(
                selectedCategory = selectedCategory,
                onCategorySelected = onCategorySelected,
                language = language
            )
        }

        // Today's Top Prices Section
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = LocalizationManager.getText("today_top_prices", language),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )

                Spacer(modifier = Modifier.height(8.dp))

                val scrollState = rememberScrollState()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(scrollState),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    topPrices.forEach { item ->
                        TopPriceMiniCard(
                            crop = item,
                            unit = unit,
                            language = language,
                            onClick = { onViewTrends(item) }
                        )
                    }
                }
            }
        }

        // Price Increased & Price Decreased Highlights (Prompt Requirement #1)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Price Increased Box
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = PriceUpBackground.copy(alpha = 0.7f)),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.TrendingUp, contentDescription = "Up", tint = PriceUpGreen, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = LocalizationManager.getText("price_increased", language),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = PriceUpGreen
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        gainers.forEach { g ->
                            Text(
                                text = "• ${g.cropName}: +%.1f%%".format(g.priceChangePercent),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF14532D)
                            )
                        }
                    }
                }

                // Price Decreased Box
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = PriceDownBackground.copy(alpha = 0.7f)),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.TrendingDown, contentDescription = "Down", tint = PriceDownRed, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = LocalizationManager.getText("price_decreased", language),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = PriceDownRed
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        losers.forEach { l ->
                            Text(
                                text = "• ${l.cropName}: %.1f%%".format(l.priceChangePercent),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF7F1D1D)
                            )
                        }
                    }
                }
            }
        }

        // Section Title: Latest Mandi Rates
        item {
            Text(
                text = "Today's Mandi Rates (${cropPrices.size} crops available):",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )
        }

        // Crop Cards List
        items(cropPrices) { crop ->
            CropPriceCard(
                cropPrice = crop,
                unit = unit,
                language = language,
                isFavorite = favoriteCropIds.contains(crop.id),
                onToggleFavorite = { onToggleFavorite(crop) },
                onShare = { onShareCrop(crop) },
                onCompare = { onCompareCrop(crop) },
                onViewTrends = { onViewTrends(crop) },
                onClick = { onViewTrends(crop) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

@Composable
fun TopPriceMiniCard(
    crop: CropPrice,
    unit: PriceUnit,
    language: AppLanguage,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val unitStr = if (unit == PriceUnit.PER_KG) "kg" else "qtl"
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .width(160.dp)
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = LocalizationManager.getLocalizedCropName(crop.cropName, language),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color(0xFF111827),
                maxLines = 1
            )
            Text(
                text = crop.marketName,
                fontSize = 11.sp,
                color = Color(0xFF6B7280),
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "₹%.1f/$unitStr".format(crop.getDisplayAvg(unit)),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp,
                color = HarvestGreenPrimary
            )
            Text(
                text = "Change: %+.1f%%".format(crop.priceChangePercent),
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (crop.isPriceUp) PriceUpGreen else PriceDownRed
            )
        }
    }
}
