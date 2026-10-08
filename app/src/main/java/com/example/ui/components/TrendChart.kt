package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PriceTrendPoint
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
fun PriceTrendSection(
    cropName: String,
    timeframe: String,
    points: List<PriceTrendPoint>,
    unit: PriceUnit,
    language: AppLanguage,
    onSelectTimeframe: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val timeframes = listOf("Today", "7 Days", "30 Days", "6 Months")
    val factor = unit.factorFromKg
    val unitStr = if (unit == PriceUnit.PER_KG) "kg" else "quintal"

    // Calculate percent change between first and last point
    val firstPrice = points.firstOrNull()?.priceKg?.times(factor) ?: 0.0
    val lastPrice = points.lastOrNull()?.priceKg?.times(factor) ?: 0.0
    val percentChange = if (firstPrice > 0) ((lastPrice - firstPrice) / firstPrice) * 100 else 0.0
    val isPositive = percentChange >= 0

    val minPrice = points.minOfOrNull { it.priceKg * factor } ?: 0.0
    val maxPrice = points.maxOfOrNull { it.priceKg * factor } ?: 0.0
    val avgPrice = if (points.isNotEmpty()) points.map { it.priceKg * factor }.average() else 0.0

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.fillMaxWidth().testTag("price_trend_section")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "$cropName Price Trend",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )
                    Text(
                        text = "Track daily market price movements",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF6B7280)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isPositive) PriceUpBackground else PriceDownBackground
                ) {
                    Text(
                        text = "${if (isPositive) "↑ +" else "↓ "}%.1f%%".format(percentChange),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isPositive) PriceUpGreen else PriceDownRed,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Timeframe Selector Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                timeframes.forEach { tf ->
                    val isSelected = tf == timeframe
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSelectTimeframe(tf) },
                        label = {
                            Text(
                                text = tf,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = HarvestGreenPrimary,
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.weight(1f).testTag("timeframe_chip_${tf.lowercase().replace(" ", "_")}")
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Conversational Explanation Banner (Required by user prompt!)
            val directionWord = if (isPositive) "increased" else "decreased"
            val conversationalMessage = when (language) {
                AppLanguage.HINDI -> "${LocalizationManager.getLocalizedCropName(cropName, language)} का भाव पिछले $timeframe में %.1f%% ${if (isPositive) "बढ़ा" else "घटा"} है।".format(Math.abs(percentChange))
                AppLanguage.KANNADA -> "$cropName ಬೆಲೆ ಕಳೆದ $timeframe ನಲ್ಲಿ %.1f%% ${if (isPositive) "ಹೆಚ್ಚಾಗಿದೆ" else "ಕಡಿಮೆಯಾಗಿದೆ"}.".format(Math.abs(percentChange))
                AppLanguage.TELUGU -> "$cropName ధర గత $timeframe లో %.1f%% ${if (isPositive) "పెరిగింది" else "తగ్గింది"}.".format(Math.abs(percentChange))
                AppLanguage.TAMIL -> "$cropName விலை கடந்த $timeframe இல் %.1f%% ${if (isPositive) "அதிகரித்துள்ளது" else "குறைந்துள்ளது"}.".format(Math.abs(percentChange))
                AppLanguage.MARATHI -> "$cropName चा भाव मागील $timeframe मध्ये %.1f%% ${if (isPositive) "वाढला" else "घसरला"} आहे.".format(Math.abs(percentChange))
                AppLanguage.ENGLISH -> "$cropName prices have $directionWord by %.1f%% in the last $timeframe.".format(Math.abs(percentChange))
            }

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = HarvestGreenContainer.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "📢", fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = conversationalMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF134E24)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Visual Canvas Chart
            if (points.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(Color(0xFFFAFAFA), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxWidth().height(140.dp)) {
                        val w = size.width
                        val h = size.height

                        val prices = points.map { it.priceKg * factor }
                        val min = prices.minOrNull() ?: 0.0
                        val max = prices.maxOrNull() ?: 1.0
                        val range = if (max == min) 1.0 else max - min

                        // Draw horizontal subtle grid lines
                        val gridCount = 3
                        for (i in 0..gridCount) {
                            val y = h * i / gridCount
                            drawLine(
                                color = Color(0xFFE5E7EB),
                                start = Offset(0f, y),
                                end = Offset(w, y),
                                strokeWidth = 1f
                            )
                        }

                        // Build line path
                        val path = Path()
                        val fillPath = Path()
                        val stepX = if (points.size > 1) w / (points.size - 1) else w

                        points.forEachIndexed { index, pt ->
                            val currentP = pt.priceKg * factor
                            val normalizedY = ((currentP - min) / range).toFloat()
                            val x = index * stepX
                            val y = h - (normalizedY * (h * 0.8f) + (h * 0.1f))

                            if (index == 0) {
                                path.moveTo(x, y)
                                fillPath.moveTo(x, h)
                                fillPath.lineTo(x, y)
                            } else {
                                path.lineTo(x, y)
                                fillPath.lineTo(x, y)
                            }

                            // Draw data point dots
                            drawCircle(
                                color = if (isPositive) HarvestGreenPrimary else PriceDownRed,
                                radius = 4.dp.toPx(),
                                center = Offset(x, y)
                            )
                            drawCircle(
                                color = Color.White,
                                radius = 2.dp.toPx(),
                                center = Offset(x, y)
                            )
                        }

                        fillPath.lineTo(w, h)
                        fillPath.close()

                        // Draw gradient area under curve
                        val areaBrush = Brush.verticalGradient(
                            colors = if (isPositive) {
                                listOf(HarvestGreenPrimary.copy(alpha = 0.35f), Color.Transparent)
                            } else {
                                listOf(PriceDownRed.copy(alpha = 0.35f), Color.Transparent)
                            }
                        )
                        drawPath(fillPath, brush = areaBrush)

                        // Draw main curve stroke
                        drawPath(
                            path = path,
                            color = if (isPositive) HarvestGreenPrimary else PriceDownRed,
                            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }

                    // Bottom Day Labels
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        points.forEach { pt ->
                            Text(
                                text = pt.dayLabel,
                                fontSize = 10.sp,
                                color = Color(0xFF6B7280),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Stat Summary Cards: Lowest, Average, Highest
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PriceSummaryStat(
                    label = "Period Min",
                    value = "₹%.1f/$unitStr".format(minPrice),
                    color = Color(0xFF4B5563),
                    modifier = Modifier.weight(1f)
                )
                PriceSummaryStat(
                    label = "Period Avg",
                    value = "₹%.1f/$unitStr".format(avgPrice),
                    color = HarvestGreenPrimary,
                    modifier = Modifier.weight(1f)
                )
                PriceSummaryStat(
                    label = "Period Max",
                    value = "₹%.1f/$unitStr".format(maxPrice),
                    color = Color(0xFF1E3A8A),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun PriceSummaryStat(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFFF9FAFB),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, fontSize = 11.sp, color = Color(0xFF6B7280))
            Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = color)
        }
    }
}
