package com.example.ui.components

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import com.example.ui.theme.HarvestGreenContainer
import com.example.ui.theme.HarvestGreenPrimary
import com.example.ui.theme.SunGold
import com.example.ui.theme.SunGoldContainer

@Composable
fun MarketComparisonView(
    cropName: String,
    allCropPrices: List<CropPrice>,
    availableCropNames: List<String>,
    onSelectCrop: (String) -> Unit,
    unit: PriceUnit,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val matches = allCropPrices.filter { it.cropName.equals(cropName, ignoreCase = true) }
    val sortedMatches = matches.sortedByDescending { it.avgPrice }
    val bestMarket = sortedMatches.firstOrNull()
    val lowestMarket = sortedMatches.lastOrNull()
    val factor = unit.factorFromKg
    val unitStr = if (unit == PriceUnit.PER_KG) "kg" else "quintal"

    Column(modifier = modifier.fillMaxWidth()) {
        // Crop Selector Chips Row
        Text(
            text = "Select Crop to Compare Mandis:",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF111827),
            modifier = Modifier.padding(bottom = 6.dp)
        )

        val scrollState = rememberScrollState()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            availableCropNames.forEach { name ->
                val isSelected = name.equals(cropName, ignoreCase = true)
                FilterChip(
                    selected = isSelected,
                    onClick = { onSelectCrop(name) },
                    label = {
                        Text(
                            text = LocalizationManager.getLocalizedCropName(name, language),
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = HarvestGreenPrimary,
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.testTag("compare_chip_${name.lowercase()}")
                )
            }
        }

        // Highlight Banner for Highest Paying Mandi (Best to Sell)
        if (bestMarket != null) {
            val bestAvg = bestMarket.getDisplayAvg(unit)
            val diff = if (lowestMarket != null && lowestMarket != bestMarket) {
                bestAvg - lowestMarket.getDisplayAvg(unit)
            } else 0.0

            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SunGoldContainer),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp)
                    .testTag("best_market_banner")
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(SunGold),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = "Trophy",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "🏆 ${LocalizationManager.getText("best_market_to_sell", language)}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF78350F)
                            )
                        }
                        Text(
                            text = "${bestMarket.marketName} (${bestMarket.district}, ${bestMarket.state})",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF451A03)
                        )
                        Text(
                            text = "Avg: ₹%.1f/$unitStr ${if (diff > 0) "(+₹%.1f/$unitStr more than lowest!)".format(diff) else ""}".format(bestAvg),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            color = HarvestGreenPrimary
                        )
                    }
                }
            }
        }

        // Comparison Table / List
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth().testTag("compare_table_card")
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                // Table Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFF3F4F6))
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Market / Mandi",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF374151),
                        modifier = Modifier.weight(1.5f)
                    )
                    Text(
                        text = "Min",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF374151),
                        modifier = Modifier.weight(0.8f)
                    )
                    Text(
                        text = "Max",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF374151),
                        modifier = Modifier.weight(0.8f)
                    )
                    Text(
                        text = "Average",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = HarvestGreenPrimary,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                if (sortedMatches.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No mandis found for $cropName yet.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                } else {
                    sortedMatches.forEachIndexed { index, m ->
                        val isTop = index == 0
                        val rowBg = if (isTop) HarvestGreenContainer.copy(alpha = 0.4f) else Color.Transparent

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(rowBg)
                                .padding(horizontal = 10.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1.5f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (isTop) {
                                        Text(text = "⭐ ", fontSize = 12.sp)
                                    }
                                    Text(
                                        text = m.marketName,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = if (isTop) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isTop) HarvestGreenPrimary else Color(0xFF1F2937),
                                        maxLines = 1
                                    )
                                }
                                Text(
                                    text = "${m.district}, ${m.state}",
                                    fontSize = 10.sp,
                                    color = Color(0xFF6B7280),
                                    maxLines = 1
                                )
                            }

                            Text(
                                text = "₹%.1f".format(m.getDisplayMin(unit)),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF4B5563),
                                modifier = Modifier.weight(0.8f)
                            )

                            Text(
                                text = "₹%.1f".format(m.getDisplayMax(unit)),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF4B5563),
                                modifier = Modifier.weight(0.8f)
                            )

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "₹%.1f".format(m.getDisplayAvg(unit)),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isTop) HarvestGreenPrimary else Color(0xFF111827)
                                )
                                Text(
                                    text = "%+.1f%%".format(m.priceChangePercent),
                                    fontSize = 10.sp,
                                    color = if (m.isPriceUp) Color(0xFF16A34A) else Color(0xFFDC2626)
                                )
                            }
                        }

                        if (index < sortedMatches.size - 1) {
                            Divider(color = Color(0xFFF3F4F6), thickness = 0.8.dp)
                        }
                    }
                }
            }
        }
    }
}
