package com.example.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.example.ui.theme.PriceDownBackground
import com.example.ui.theme.PriceDownRed
import com.example.ui.theme.PriceUpBackground
import com.example.ui.theme.PriceUpGreen

@Composable
fun CropPriceCard(
    cropPrice: CropPrice,
    unit: PriceUnit,
    language: AppLanguage,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onShare: () -> Unit,
    onCompare: () -> Unit,
    onViewTrends: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val displayMin = cropPrice.getDisplayMin(unit)
    val displayMax = cropPrice.getDisplayMax(unit)
    val displayAvg = cropPrice.getDisplayAvg(unit)
    val unitStr = if (unit == PriceUnit.PER_KG) "kg" else "qtl"

    val localizedCropName = LocalizationManager.getLocalizedCropName(cropPrice.cropName, language)

    val iconEmoji = when (cropPrice.cropName.lowercase()) {
        "tomato" -> "🍅"
        "onion" -> "🧅"
        "potato" -> "🥔"
        "rice" -> "🌾"
        "wheat" -> "🌾"
        "mango" -> "🥭"
        "banana" -> "🍌"
        "cotton" -> "🌱"
        "soybean" -> "🫘"
        "chilli" -> "🌶️"
        "garlic" -> "🧄"
        "ginger" -> "🫚"
        "turmeric" -> "✨"
        "mustard" -> "🌻"
        "maize" -> "🌽"
        else -> "🌱"
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("crop_price_card_${cropPrice.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Emoji icon, Crop Name, Market Name, Favorite
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(HarvestGreenContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = iconEmoji, fontSize = 26.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = localizedCropName,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF111827)
                            )
                        }
                        Text(
                            text = "${cropPrice.marketName} • ${cropPrice.district}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF4B5563)
                        )
                    }
                }

                // Favorite and Share icon buttons
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onToggleFavorite,
                        modifier = Modifier.size(36.dp).testTag("fav_button_${cropPrice.id}")
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) Color(0xFFDC2626) else Color(0xFF9CA3AF),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    IconButton(
                        onClick = onShare,
                        modifier = Modifier.size(36.dp).testTag("share_button_${cropPrice.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = HarvestGreenPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Pricing 3-Box Grid: Min, Average (Large Highlight), Max
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF9FAFB))
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Min Price
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    Text(
                        text = LocalizationManager.getText("min_price", language),
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFF6B7280)
                    )
                    Text(
                        text = "₹%.1f".format(displayMin),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF374151)
                    )
                    Text(
                        text = "/$unitStr",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF9CA3AF)
                    )
                }

                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(36.dp)
                        .background(Color(0xFFE5E7EB))
                )

                // Average Price (Main Highlight)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1.3f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(HarvestGreenContainer.copy(alpha = 0.5f))
                        .padding(vertical = 4.dp)
                ) {
                    Text(
                        text = LocalizationManager.getText("avg_price", language),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = HarvestGreenPrimary
                    )
                    Text(
                        text = "₹%.1f".format(displayAvg),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = HarvestGreenPrimary
                    )
                    Text(
                        text = "/$unitStr",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = HarvestGreenPrimary
                    )
                }

                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(36.dp)
                        .background(Color(0xFFE5E7EB))
                )

                // Max Price
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    Text(
                        text = LocalizationManager.getText("max_price", language),
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFF6B7280)
                    )
                    Text(
                        text = "₹%.1f".format(displayMax),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF374151)
                    )
                    Text(
                        text = "/$unitStr",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF9CA3AF)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Footer Row: Price Change badge + Last updated time + Action pills
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Price change badge
                val isUp = cropPrice.isPriceUp
                val badgeBg = if (isUp) PriceUpBackground else PriceDownBackground
                val badgeColor = if (isUp) PriceUpGreen else PriceDownRed
                val arrow = if (isUp) "↑" else "↓"

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = badgeBg,
                    modifier = Modifier.clip(RoundedCornerShape(16.dp))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "$arrow %+.1f%%".format(cropPrice.priceChangePercent),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = badgeColor
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "vs yesterday",
                            style = MaterialTheme.typography.labelSmall,
                            color = badgeColor.copy(alpha = 0.8f)
                        )
                    }
                }

                // Update timestamp
                Text(
                    text = cropPrice.lastUpdated,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF6B7280)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Quick action buttons: Compare Markets & Price Trends
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onCompare,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                        .testTag("compare_button_${cropPrice.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.CompareArrows,
                        contentDescription = "Compare",
                        tint = HarvestGreenPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Compare",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = HarvestGreenPrimary
                    )
                }

                OutlinedButton(
                    onClick = onViewTrends,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                        .testTag("trends_button_${cropPrice.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.ShowChart,
                        contentDescription = "Trends",
                        tint = HarvestGreenPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Price Trends",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = HarvestGreenPrimary
                    )
                }
            }
        }
    }
}
