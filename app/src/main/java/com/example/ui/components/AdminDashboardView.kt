package com.example.ui.components

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
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
import com.example.ui.theme.HarvestGreenContainer
import com.example.ui.theme.HarvestGreenPrimary
import com.example.ui.theme.SunGold
import com.example.ui.theme.SunGoldContainer

@Composable
fun AdminDashboardView(
    allPrices: List<CropPrice>,
    onAddPrice: (String, String, String, String, String, Double, Double, Double) -> Unit,
    onUpdatePrice: (CropPrice) -> Unit,
    onDeletePrice: (CropPrice) -> Unit,
    onResetData: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var editingCrop by remember { mutableStateOf<CropPrice?>(null) }
    var showResetConfirm by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        // Admin Header Banner
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth().testTag("admin_banner_card")
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(SunGold),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = "Admin",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "Admin Price Management",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Add, edit, or delete Mandi records (${allPrices.size} total)",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Action Buttons Row: Add Record & Reset Data
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = { showAddDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = HarvestGreenPrimary),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f).testTag("admin_add_record_button")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Add Crop Rate", fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { showResetConfirm = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF475569)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f).testTag("admin_reset_data_button")
            ) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = "Reset", modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Reset Defaults", fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Active Market Records:",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F2937)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // List of all items with edit and delete actions
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            allPrices.take(15).forEach { price ->
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
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${price.cropName} (${price.category})",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "${price.marketName} • ${price.district}, ${price.state}",
                                fontSize = 12.sp,
                                color = Color(0xFF6B7280)
                            )
                            Text(
                                text = "Avg: ₹%.1f/kg | Min: ₹%.1f | Max: ₹%.1f".format(price.avgPrice, price.minPrice, price.maxPrice),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = HarvestGreenPrimary
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { editingCrop = price }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit",
                                    tint = Color(0xFF2563EB),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            IconButton(onClick = { onDeletePrice(price) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = Color(0xFFDC2626),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Add Record Dialog
    if (showAddDialog) {
        var cropNameInput by remember { mutableStateOf("") }
        var categoryInput by remember { mutableStateOf("Vegetables") }
        var stateInput by remember { mutableStateOf("Karnataka") }
        var distInput by remember { mutableStateOf("Bengaluru") }
        var marketInput by remember { mutableStateOf("Yeshwanthpur APMC") }
        var minInput by remember { mutableStateOf("25") }
        var maxInput by remember { mutableStateOf("40") }
        var avgInput by remember { mutableStateOf("32") }

        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Add New Mandi Price Record", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = cropNameInput,
                        onValueChange = { cropNameInput = it },
                        label = { Text("Crop Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = categoryInput,
                        onValueChange = { categoryInput = it },
                        label = { Text("Category (Vegetables, Fruits, Cereals)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = stateInput,
                        onValueChange = { stateInput = it },
                        label = { Text("State") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = distInput,
                        onValueChange = { distInput = it },
                        label = { Text("District") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = marketInput,
                        onValueChange = { marketInput = it },
                        label = { Text("Market / Mandi Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        OutlinedTextField(
                            value = minInput,
                            onValueChange = { minInput = it },
                            label = { Text("Min ₹/kg") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = avgInput,
                            onValueChange = { avgInput = it },
                            label = { Text("Avg ₹/kg") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = maxInput,
                            onValueChange = { maxInput = it },
                            label = { Text("Max ₹/kg") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val min = minInput.toDoubleOrNull() ?: 20.0
                        val max = maxInput.toDoubleOrNull() ?: 35.0
                        val avg = avgInput.toDoubleOrNull() ?: 28.0
                        if (cropNameInput.isNotBlank()) {
                            onAddPrice(cropNameInput, categoryInput, stateInput, distInput, marketInput, min, max, avg)
                        }
                        showAddDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = HarvestGreenPrimary)
                ) {
                    Text("Save Record")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Edit Record Dialog
    editingCrop?.let { crop ->
        var editMin by remember { mutableStateOf(crop.minPrice.toString()) }
        var editMax by remember { mutableStateOf(crop.maxPrice.toString()) }
        var editAvg by remember { mutableStateOf(crop.avgPrice.toString()) }

        AlertDialog(
            onDismissRequest = { editingCrop = null },
            title = { Text("Edit Price: ${crop.cropName} (${crop.marketName})", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = editMin,
                        onValueChange = { editMin = it },
                        label = { Text("Min Price (₹/kg)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editAvg,
                        onValueChange = { editAvg = it },
                        label = { Text("Average Price (₹/kg)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editMax,
                        onValueChange = { editMax = it },
                        label = { Text("Max Price (₹/kg)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val min = editMin.toDoubleOrNull() ?: crop.minPrice
                        val max = editMax.toDoubleOrNull() ?: crop.maxPrice
                        val avg = editAvg.toDoubleOrNull() ?: crop.avgPrice
                        onUpdatePrice(crop.copy(minPrice = min, maxPrice = max, avgPrice = avg))
                        editingCrop = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = HarvestGreenPrimary)
                ) {
                    Text("Update Price")
                }
            },
            dismissButton = {
                TextButton(onClick = { editingCrop = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Reset Confirmation Dialog
    if (showResetConfirm) {
        AlertDialog(
            onDismissRequest = { showResetConfirm = false },
            title = { Text("Reset to Sample Dataset?", fontWeight = FontWeight.Bold) },
            text = {
                Text("This will restore default Agmarknet simulated sample prices across all states and mandis.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        onResetData()
                        showResetConfirm = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                ) {
                    Text("Confirm Reset")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetConfirm = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
