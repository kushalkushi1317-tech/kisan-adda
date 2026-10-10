package com.example.data.local

import com.example.data.model.CropPrice
import com.example.data.model.MyCrop
import com.example.data.model.PriceAlert
import com.example.data.model.PriceTrendPoint

object SampleData {

    val defaultCropPrices = listOf(
        // Tomato records across multiple mandis
        CropPrice(
            id = 1,
            cropName = "Tomato",
            category = "Vegetables",
            state = "Karnataka",
            district = "Bengaluru Urban",
            marketName = "Yeshwanthpur APMC",
            minPrice = 25.0,
            maxPrice = 38.0,
            avgPrice = 31.0,
            priceChangePercent = 8.0,
            lastUpdated = "Today, 10:30 AM",
            isSampleData = true
        ),
        CropPrice(
            id = 2,
            cropName = "Tomato",
            category = "Vegetables",
            state = "Karnataka",
            district = "Kolar",
            marketName = "Kolar APMC Mandi",
            minPrice = 28.0,
            maxPrice = 42.0,
            avgPrice = 35.0,
            priceChangePercent = 12.5,
            lastUpdated = "Today, 09:45 AM",
            isSampleData = true
        ),
        CropPrice(
            id = 3,
            cropName = "Tomato",
            category = "Vegetables",
            state = "Maharashtra",
            district = "Pune",
            marketName = "Pune Gultekdi Market",
            minPrice = 22.0,
            maxPrice = 34.0,
            avgPrice = 28.0,
            priceChangePercent = -3.2,
            lastUpdated = "Today, 11:15 AM",
            isSampleData = true
        ),
        CropPrice(
            id = 4,
            cropName = "Tomato",
            category = "Vegetables",
            state = "Delhi",
            district = "North Delhi",
            marketName = "Azadpur Mandi",
            minPrice = 30.0,
            maxPrice = 45.0,
            avgPrice = 37.5,
            priceChangePercent = 5.6,
            lastUpdated = "Today, 08:20 AM",
            isSampleData = true
        ),

        // Onion records
        CropPrice(
            id = 5,
            cropName = "Onion",
            category = "Vegetables",
            state = "Maharashtra",
            district = "Nashik",
            marketName = "Lasalgaon APMC",
            minPrice = 22.0,
            maxPrice = 32.0,
            avgPrice = 27.0,
            priceChangePercent = -5.4,
            lastUpdated = "Today, 10:15 AM",
            isSampleData = true
        ),
        CropPrice(
            id = 6,
            cropName = "Onion",
            category = "Vegetables",
            state = "Karnataka",
            district = "Bengaluru Urban",
            marketName = "Yeshwanthpur APMC",
            minPrice = 26.0,
            maxPrice = 36.0,
            avgPrice = 31.0,
            priceChangePercent = 3.3,
            lastUpdated = "Today, 10:00 AM",
            isSampleData = true
        ),
        CropPrice(
            id = 7,
            cropName = "Onion",
            category = "Vegetables",
            state = "Delhi",
            district = "North Delhi",
            marketName = "Azadpur Mandi",
            minPrice = 28.0,
            maxPrice = 38.0,
            avgPrice = 33.0,
            priceChangePercent = 4.1,
            lastUpdated = "Today, 07:50 AM",
            isSampleData = true
        ),

        // Potato records
        CropPrice(
            id = 8,
            cropName = "Potato",
            category = "Vegetables",
            state = "Uttar Pradesh",
            district = "Agra",
            marketName = "Agra Fatehabad Mandi",
            minPrice = 18.0,
            maxPrice = 25.0,
            avgPrice = 21.5,
            priceChangePercent = 6.2,
            lastUpdated = "Today, 09:30 AM",
            isSampleData = true
        ),
        CropPrice(
            id = 9,
            cropName = "Potato",
            category = "Vegetables",
            state = "West Bengal",
            district = "Hooghly",
            marketName = "Champadanga Mandi",
            minPrice = 16.0,
            maxPrice = 22.0,
            avgPrice = 19.0,
            priceChangePercent = -2.1,
            lastUpdated = "Today, 08:45 AM",
            isSampleData = true
        ),
        CropPrice(
            id = 10,
            cropName = "Potato",
            category = "Vegetables",
            state = "Punjab",
            district = "Jalandhar",
            marketName = "Jalandhar APMC",
            minPrice = 19.0,
            maxPrice = 26.0,
            avgPrice = 22.5,
            priceChangePercent = 4.8,
            lastUpdated = "Today, 10:40 AM",
            isSampleData = true
        ),

        // Mango & Fruits
        CropPrice(
            id = 11,
            cropName = "Mango",
            category = "Fruits",
            state = "Maharashtra",
            district = "Ratnagiri",
            marketName = "Ratnagiri Mandi (Alphonso)",
            minPrice = 90.0,
            maxPrice = 160.0,
            avgPrice = 125.0,
            priceChangePercent = 9.2,
            lastUpdated = "Today, 11:00 AM",
            isSampleData = true
        ),
        CropPrice(
            id = 12,
            cropName = "Mango",
            category = "Fruits",
            state = "Karnataka",
            district = "Kolar",
            marketName = "Srinivaspur Mango Mandi",
            minPrice = 65.0,
            maxPrice = 115.0,
            avgPrice = 90.0,
            priceChangePercent = 0.0,
            lastUpdated = "Today, 09:15 AM",
            isSampleData = true
        ),
        CropPrice(
            id = 13,
            cropName = "Banana",
            category = "Fruits",
            state = "Tamil Nadu",
            district = "Tiruchirappalli",
            marketName = "Trichy Banana Market",
            minPrice = 22.0,
            maxPrice = 35.0,
            avgPrice = 28.5,
            priceChangePercent = 3.6,
            lastUpdated = "Today, 10:20 AM",
            isSampleData = true
        ),
        CropPrice(
            id = 14,
            cropName = "Banana",
            category = "Fruits",
            state = "Maharashtra",
            district = "Jalgaon",
            marketName = "Jalgaon APMC",
            minPrice = 18.0,
            maxPrice = 28.0,
            avgPrice = 23.0,
            priceChangePercent = -4.2,
            lastUpdated = "Today, 08:30 AM",
            isSampleData = true
        ),

        // Cereals (Rice & Wheat & Maize)
        CropPrice(
            id = 15,
            cropName = "Rice",
            category = "Grains",
            state = "Punjab",
            district = "Ludhiana",
            marketName = "Khanna Grain Market",
            minPrice = 34.0,
            maxPrice = 48.0,
            avgPrice = 41.0,
            priceChangePercent = 2.5,
            lastUpdated = "Today, 11:30 AM",
            isSampleData = true
        ),
        CropPrice(
            id = 16,
            cropName = "Rice",
            category = "Grains",
            state = "Andhra Pradesh",
            district = "East Godavari",
            marketName = "Rajahmundry APMC",
            minPrice = 30.0,
            maxPrice = 44.0,
            avgPrice = 37.0,
            priceChangePercent = 1.4,
            lastUpdated = "Today, 09:00 AM",
            isSampleData = true
        ),
        CropPrice(
            id = 17,
            cropName = "Wheat",
            category = "Grains",
            state = "Madhya Pradesh",
            district = "Sehore",
            marketName = "Sehore Sharbati Mandi",
            minPrice = 28.0,
            maxPrice = 42.0,
            avgPrice = 35.0,
            priceChangePercent = 4.5,
            lastUpdated = "Today, 10:50 AM",
            isSampleData = true
        ),
        CropPrice(
            id = 18,
            cropName = "Wheat",
            category = "Grains",
            state = "Haryana",
            district = "Karnal",
            marketName = "Karnal Grain Mandi",
            minPrice = 24.0,
            maxPrice = 32.0,
            avgPrice = 28.0,
            priceChangePercent = -1.8,
            lastUpdated = "Today, 09:10 AM",
            isSampleData = true
        ),
        CropPrice(
            id = 19,
            cropName = "Maize",
            category = "Grains",
            state = "Karnataka",
            district = "Davanagere",
            marketName = "Davanagere APMC",
            minPrice = 20.0,
            maxPrice = 26.5,
            avgPrice = 23.5,
            priceChangePercent = 5.0,
            lastUpdated = "Today, 08:40 AM",
            isSampleData = true
        ),

        // Pulses & Oilseeds
        CropPrice(
            id = 20,
            cropName = "Soybean",
            category = "Pulses",
            state = "Madhya Pradesh",
            district = "Indore",
            marketName = "Indore Choithram Mandi",
            minPrice = 42.0,
            maxPrice = 54.0,
            avgPrice = 48.0,
            priceChangePercent = 6.7,
            lastUpdated = "Today, 10:15 AM",
            isSampleData = true
        ),
        CropPrice(
            id = 21,
            cropName = "Soybean",
            category = "Pulses",
            state = "Maharashtra",
            district = "Latur",
            marketName = "Latur APMC",
            minPrice = 44.0,
            maxPrice = 56.0,
            avgPrice = 50.5,
            priceChangePercent = 8.1,
            lastUpdated = "Today, 09:55 AM",
            isSampleData = true
        ),
        CropPrice(
            id = 22,
            cropName = "Mustard",
            category = "Other Crops",
            state = "Rajasthan",
            district = "Bharatpur",
            marketName = "Bharatpur Mandi",
            minPrice = 52.0,
            maxPrice = 64.0,
            avgPrice = 58.5,
            priceChangePercent = 3.8,
            lastUpdated = "Today, 11:05 AM",
            isSampleData = true
        ),
        CropPrice(
            id = 23,
            cropName = "Cotton",
            category = "Other Crops",
            state = "Gujarat",
            district = "Rajkot",
            marketName = "Rajkot APMC (Kapas)",
            minPrice = 68.0,
            maxPrice = 85.0,
            avgPrice = 77.0,
            priceChangePercent = -2.8,
            lastUpdated = "Today, 10:45 AM",
            isSampleData = true
        ),

        // Spices
        CropPrice(
            id = 24,
            cropName = "Chilli",
            category = "Spices",
            state = "Andhra Pradesh",
            district = "Guntur",
            marketName = "Guntur Mirchi Yard",
            minPrice = 140.0,
            maxPrice = 210.0,
            avgPrice = 175.0,
            priceChangePercent = 14.2,
            lastUpdated = "Today, 10:05 AM",
            isSampleData = true
        ),
        CropPrice(
            id = 25,
            cropName = "Garlic",
            category = "Spices",
            state = "Madhya Pradesh",
            district = "Mandsaur",
            marketName = "Mandsaur APMC",
            minPrice = 110.0,
            maxPrice = 185.0,
            avgPrice = 145.0,
            priceChangePercent = 11.5,
            lastUpdated = "Today, 09:25 AM",
            isSampleData = true
        ),
        CropPrice(
            id = 26,
            cropName = "Turmeric",
            category = "Spices",
            state = "Tamil Nadu",
            district = "Erode",
            marketName = "Erode Turmeric Market",
            minPrice = 120.0,
            maxPrice = 170.0,
            avgPrice = 148.0,
            priceChangePercent = 4.2,
            lastUpdated = "Today, 08:50 AM",
            isSampleData = true
        ),
        CropPrice(
            id = 27,
            cropName = "Ginger",
            category = "Spices",
            state = "Kerala",
            district = "Wayanad",
            marketName = "Kalpetta APMC",
            minPrice = 75.0,
            maxPrice = 120.0,
            avgPrice = 95.0,
            priceChangePercent = -6.5,
            lastUpdated = "Today, 11:20 AM",
            isSampleData = true
        )
    )

    fun getTrendHistoryForCrop(cropName: String, baseAvg: Double): Map<String, List<PriceTrendPoint>> {
        val today = baseAvg
        val day7 = listOf(
            PriceTrendPoint("Day 1", baseAvg * 0.90),
            PriceTrendPoint("Day 2", baseAvg * 0.92),
            PriceTrendPoint("Day 3", baseAvg * 0.91),
            PriceTrendPoint("Day 4", baseAvg * 0.95),
            PriceTrendPoint("Day 5", baseAvg * 0.98),
            PriceTrendPoint("Day 6", baseAvg * 0.99),
            PriceTrendPoint("Today", today)
        )
        val day30 = listOf(
            PriceTrendPoint("Wk 1", baseAvg * 0.84),
            PriceTrendPoint("Wk 2", baseAvg * 0.88),
            PriceTrendPoint("Wk 3", baseAvg * 0.93),
            PriceTrendPoint("Wk 4", baseAvg * 0.97),
            PriceTrendPoint("Today", today)
        )
        val month6 = listOf(
            PriceTrendPoint("May", baseAvg * 0.78),
            PriceTrendPoint("Jun", baseAvg * 0.82),
            PriceTrendPoint("Jul", baseAvg * 0.89),
            PriceTrendPoint("Aug", baseAvg * 0.94),
            PriceTrendPoint("Sep", baseAvg * 0.97),
            PriceTrendPoint("Oct", today)
        )
        val todayTrend = listOf(
            PriceTrendPoint("08 AM", baseAvg * 0.96),
            PriceTrendPoint("10 AM", baseAvg * 0.98),
            PriceTrendPoint("12 PM", today),
            PriceTrendPoint("02 PM", baseAvg * 1.01)
        )
        return mapOf(
            "Today" to todayTrend,
            "7 Days" to day7,
            "30 Days" to day30,
            "6 Months" to month6
        )
    }

    val defaultMyCrops = listOf(
        MyCrop(id = 1, cropName = "Tomato", quantityAcreOrBags = "3 Acres", targetPricePerKg = 32.0, notes = "Harvesting in 4 days"),
        MyCrop(id = 2, cropName = "Onion", quantityAcreOrBags = "50 Quintals", targetPricePerKg = 30.0, notes = "Stored in shed"),
        MyCrop(id = 3, cropName = "Wheat", quantityAcreOrBags = "5 Acres", targetPricePerKg = 36.0, notes = "Ready for selling")
    )

    val defaultAlerts = listOf(
        PriceAlert(id = 1, cropName = "Tomato", targetPricePerKg = 35.0, alertWhenAbove = true),
        PriceAlert(id = 2, cropName = "Onion", targetPricePerKg = 25.0, alertWhenAbove = false)
    )
}
