package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class PriceUnit(val label: String, val factorFromKg: Double) {
    PER_KG("₹/kg", 1.0),
    PER_QUINTAL("₹/quintal", 100.0)
}

enum class CropCategory(val id: String, val icon: String) {
    ALL("All", "🌾"),
    VEGETABLES("Vegetables", "🥦"),
    FRUITS("Fruits", "🍎"),
    CEREALS("Cereals", "🌾"),
    PULSES("Pulses", "🫘"),
    SPICES("Spices", "🌶️"),
    OTHER("Other Crops", "🌱")
}

@Entity(tableName = "crop_prices")
data class CropPrice(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val cropName: String,
    val category: String,
    val state: String,
    val district: String,
    val marketName: String,
    val minPrice: Double, // Base stored in ₹/kg
    val maxPrice: Double, // Base stored in ₹/kg
    val avgPrice: Double, // Base stored in ₹/kg
    val priceChangePercent: Double, // e.g. +8.2 or -3.5
    val lastUpdated: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isSampleData: Boolean = true
) {
    fun getDisplayMin(unit: PriceUnit): Double = minPrice * unit.factorFromKg
    fun getDisplayMax(unit: PriceUnit): Double = maxPrice * unit.factorFromKg
    fun getDisplayAvg(unit: PriceUnit): Double = avgPrice * unit.factorFromKg

    val isPriceUp: Boolean get() = priceChangePercent >= 0.0
}

data class PriceTrendPoint(
    val dayLabel: String,
    val priceKg: Double,
    val isProjected: Boolean = false
)

@Entity(tableName = "favorites")
data class FavoriteItem(
    @PrimaryKey
    val cropId: Long,
    val cropName: String,
    val marketName: String
)

@Entity(tableName = "my_crops")
data class MyCrop(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val cropName: String,
    val quantityAcreOrBags: String = "5 Acres",
    val targetPricePerKg: Double = 30.0,
    val notes: String = ""
)

@Entity(tableName = "price_alerts")
data class PriceAlert(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val cropName: String,
    val targetPricePerKg: Double,
    val alertWhenAbove: Boolean = true,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)
