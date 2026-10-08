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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.example.data.model.CropPrice
import com.example.data.model.MyCrop
import com.example.data.model.PriceAlert
import com.example.data.model.PriceUnit
import com.example.localization.AppLanguage
import com.example.localization.LocalizationManager
import com.example.ui.theme.HarvestGreenContainer
import com.example.ui.theme.HarvestGreenPrimary
import com.example.ui.theme.PriceUpGreen
import com.example.ui.theme.SunGold
import com.example.ui.theme.SunGoldContainer

@Composable
fun FarmerDashboardView(
    myCrops: List<MyCrop>,
    allPrices: List<CropPrice>,
    priceAlerts: List<PriceAlert>,
    unit: PriceUnit,
    language: AppLanguage,
    onAddMyCrop: (String, String, Double, String) -> Unit,
    onDeleteMyCrop: (MyCrop) -> Unit,
    onAddAlert: (String, Double, Boolean) -> Unit,
    onDeleteAlert: (PriceAlert) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddCropDialog by remember { mutableStateOf(false) }
    var showAddAlertDialog by remember { mutableStateOf(false) }

    val factor = unit.factorFromKg
    val unitStr = if (unit == PriceUnit.PER_KG) "kg" else "quintal"

    Column(modifier = modifier.fillMaxWidth()) {
        // Smart Selling Recommendation Banner (Prompt Requirement #6)
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = HarvestGreenContainer.copy(alpha = 0.7f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth().testTag("smart_sell_alert_card")
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(HarvestGreenPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.TrendingUp,
                        contentDescription = "Alert",
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "💡 Smart Mandi Selling Alert",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF134E24)
                    )
                    Text(
                        text = "Tomato price is higher at Kolar APMC (₹35/kg) than Pune Gultekdi (₹28/kg). You may get up to ₹700 more per quintal at Kolar APMC!",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1B4332)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // "My Crops" Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = LocalizationManager.getText("my_crops", language),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )
                Text(
                    text = "Track the produce you are growing or holding",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF6B7280)
                )
            }

            Button(
                onClick = { showAddCropDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = HarvestGreenPrimary),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.testTag("add_my_crop_button")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Add Crop", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // My Crops List
        if (myCrops.isEmpty()) {
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                Box(modifier = Modifier.padding(24.dp), contentAlignment = Alignment.Center) {
                    Text(
                        text = "No crops added yet. Tap 'Add Crop' to track your harvest!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                myCrops.forEach { crop ->
                    val matchingPrices = allPrices.filter { it.cropName.equals(crop.cropName, ignoreCase = true) }
                    val avgMandiPrice = matchingPrices.maxByOrNull { it.avgPrice }?.getDisplayAvg(unit) ?: 0.0
                    val topMandi = matchingPrices.maxByOrNull { it.avgPrice }?.marketName ?: "Various Mandis"
                    val isTargetMet = avgMandiPrice >= (crop.targetPricePerKg * factor)

                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth().testTag("my_crop_item_${crop.id}")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "${crop.cropName} (${crop.quantityAcreOrBags})",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF111827)
                                    )
                                    if (isTargetMet) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = HarvestGreenContainer
                                        ) {
                                            Text(
                                                text = "Target Met! 🎯",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = HarvestGreenPrimary,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                                Text(
                                    text = "Current Top Mandi: $topMandi",
                                    fontSize = 12.sp,
                                    color = Color(0xFF6B7280)
                                )
                                Text(
                                    text = "Target Price: ₹%.1f/$unitStr | Notes: %s".format(crop.targetPricePerKg * factor, if (crop.notes.isNotEmpty()) crop.notes else "None"),
                                    fontSize = 11.sp,
                                    color = Color(0xFF4B5563)
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "₹%.1f/$unitStr".format(avgMandiPrice),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isTargetMet) PriceUpGreen else Color(0xFF111827)
                                )

                                IconButton(
                                    onClick = { onDeleteMyCrop(crop) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = Color(0xFFEF4444),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Price Alerts Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Price Alerts",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )
                Text(
                    text = "Get notified when prices hit your selling threshold",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF6B7280)
                )
            }

            Button(
                onClick = { showAddAlertDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = SunGold),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.testTag("add_alert_button")
            ) {
                Icon(imageVector = Icons.Default.NotificationsActive, contentDescription = "Alert", modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Set Alert", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Price Alerts List
        if (priceAlerts.isEmpty()) {
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                Box(modifier = Modifier.padding(20.dp), contentAlignment = Alignment.Center) {
                    Text(
                        text = "No price alerts set. Tap 'Set Alert' to monitor targets.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                priceAlerts.forEach { alert ->
                    val condition = if (alert.alertWhenAbove) "goes above" else "drops below"
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(SunGoldContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.NotificationsActive,
                                        contentDescription = "Alert",
                                        tint = SunGold,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "${alert.cropName} Alert",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = "Notify when price $condition ₹%.1f/kg".format(alert.targetPricePerKg),
                                        fontSize = 12.sp,
                                        color = Color(0xFF4B5563)
                                    )
                                }
                            }

                            IconButton(
                                onClick = { onDeleteAlert(alert) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = Color(0xFF9CA3AF),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Add Crop Dialog
    if (showAddCropDialog) {
        var cropNameInput by remember { mutableStateOf("Tomato") }
        var quantityInput by remember { mutableStateOf("2 Acres") }
        var targetPriceInput by remember { mutableStateOf("30") }
        var notesInput by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddCropDialog = false },
            title = { Text("Add Crop to Dashboard", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = cropNameInput,
                        onValueChange = { cropNameInput = it },
                        label = { Text("Crop Name (e.g. Tomato, Onion)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = quantityInput,
                        onValueChange = { quantityInput = it },
                        label = { Text("Quantity or Farm Area (e.g. 5 Acres, 100 Qtl)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = targetPriceInput,
                        onValueChange = { targetPriceInput = it },
                        label = { Text("Target Sell Price (₹/kg)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = notesInput,
                        onValueChange = { notesInput = it },
                        label = { Text("Notes (e.g. Harvesting next week)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val target = targetPriceInput.toDoubleOrNull() ?: 30.0
                        onAddMyCrop(cropNameInput, quantityInput, target, notesInput)
                        showAddCropDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = HarvestGreenPrimary)
                ) {
                    Text("Save Crop")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddCropDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Add Price Alert Dialog
    if (showAddAlertDialog) {
        var alertCropInput by remember { mutableStateOf("Tomato") }
        var alertPriceInput by remember { mutableStateOf("35") }

        AlertDialog(
            onDismissRequest = { showAddAlertDialog = false },
            title = { Text("Set Price Alert", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = alertCropInput,
                        onValueChange = { alertCropInput = it },
                        label = { Text("Crop Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = alertPriceInput,
                        onValueChange = { alertPriceInput = it },
                        label = { Text("Alert Price Threshold (₹/kg)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val p = alertPriceInput.toDoubleOrNull() ?: 30.0
                        onAddAlert(alertCropInput, p, true)
                        showAddAlertDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SunGold)
                ) {
                    Text("Set Alert")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddAlertDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
