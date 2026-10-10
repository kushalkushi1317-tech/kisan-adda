package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Domain
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.CropCategory
import com.example.data.model.CropPrice
import com.example.data.model.PriceUnit
import com.example.localization.AppLanguage
import com.example.localization.LocalizationManager
import com.example.ui.components.CategorySelectorRow
import com.example.ui.components.CropPriceCard
import com.example.ui.components.LanguageDialog
import com.example.ui.components.LocationSelectorCard
import com.example.ui.components.MarketComparisonView
import com.example.ui.components.PriceTrendSection
import com.example.ui.theme.HarvestGreenContainer
import com.example.ui.theme.HarvestGreenPrimary
import com.example.ui.theme.SunGold
import com.example.ui.theme.SunGoldContainer
import com.example.ui.viewmodel.KisanPriceViewModel
import com.example.ui.viewmodel.SortOption

@Composable
fun MainAppScreen(
    viewModel: KisanPriceViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // Navigation and Modal States
    var currentWebSection by remember { mutableStateOf("home") }
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showLoginModal by remember { mutableStateOf(false) }
    var viewMode by remember { mutableStateOf("table") } // "table" or "cards"
    var selectedCropForModal by remember { mutableStateOf<CropPrice?>(null) }

    // ViewModel States
    val language by viewModel.selectedLanguage.collectAsStateWithLifecycle()
    val unit by viewModel.selectedUnit.collectAsStateWithLifecycle()
    val query by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val selectedState by viewModel.selectedState.collectAsStateWithLifecycle()
    val selectedDistrict by viewModel.selectedDistrict.collectAsStateWithLifecycle()
    val selectedMarket by viewModel.selectedMarket.collectAsStateWithLifecycle()
    val sortBy by viewModel.sortBy.collectAsStateWithLifecycle()

    val filteredPrices by viewModel.filteredPrices.collectAsStateWithLifecycle()
    val allPrices by viewModel.allPrices.collectAsStateWithLifecycle()
    val availableStates by viewModel.availableStates.collectAsStateWithLifecycle()
    val availableDistricts by viewModel.availableDistricts.collectAsStateWithLifecycle()
    val availableMarkets by viewModel.availableMarkets.collectAsStateWithLifecycle()
    val uniqueCropNames by viewModel.uniqueCropNames.collectAsStateWithLifecycle()

    val trendCropName by viewModel.trendCropName.collectAsStateWithLifecycle()
    val trendTimeframe by viewModel.trendTimeframe.collectAsStateWithLifecycle()
    val compareCropName by viewModel.compareCropName.collectAsStateWithLifecycle()

    val factor = unit.factorFromKg
    val unitStr = if (unit == PriceUnit.PER_KG) "kg" else "qtl"

    fun shareText(text: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        context.startActivity(Intent.createChooser(sendIntent, "Share Mandi Rates"))
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {

            // ─────────────────────────────────────────────────────────────
            // 1. DESKTOP WEBSITE HEADER (Full width, responsive desktop bar)
            // ─────────────────────────────────────────────────────────────
            Surface(
                color = Color.White,
                tonalElevation = 2.dp,
                shadowElevation = 1.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 1280.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Brand & Tagline
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable { currentWebSection = "home" }
                                .testTag("web_brand_header")
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(HarvestGreenPrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "🌾", fontSize = 26.sp)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Kissan Adda",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Black,
                                        color = Color(0xFF111827)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Surface(
                                        color = HarvestGreenContainer,
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text(
                                            text = "Portal",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = HarvestGreenPrimary,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = LocalizationManager.getTagline(language),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF6B7280),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        // Navigation Links (Desktop Menu)
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            WebNavLink(
                                title = "Home",
                                isSelected = currentWebSection == "home",
                                onClick = { currentWebSection = "home" }
                            )
                            WebNavLink(
                                title = "Crop Prices",
                                isSelected = currentWebSection == "prices",
                                onClick = { currentWebSection = "prices" }
                            )
                            WebNavLink(
                                title = "Market Locations",
                                isSelected = currentWebSection == "locations",
                                onClick = { currentWebSection = "locations" }
                            )
                            WebNavLink(
                                title = "Price Trends",
                                isSelected = currentWebSection == "trends",
                                onClick = { currentWebSection = "trends" }
                            )
                            WebNavLink(
                                title = "About Us",
                                isSelected = currentWebSection == "about",
                                onClick = { currentWebSection = "about" }
                            )
                        }

                        // Right Header Actions
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Language Selector
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFF3F4F6),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { showLanguageDialog = true }
                                    .testTag("desktop_lang_selector")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Language,
                                        contentDescription = "Language",
                                        tint = Color(0xFF4B5563),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = language.nativeName,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1F2937)
                                    )
                                }
                            }

                            // Unit Switcher
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = SunGoldContainer,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { viewModel.toggleUnit() }
                                    .testTag("desktop_unit_toggle")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.SwapHoriz,
                                        contentDescription = "Unit",
                                        tint = Color(0xFF92400E),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = unit.label,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF92400E)
                                    )
                                }
                            }

                            // Farmer Login / Profile
                            Button(
                                onClick = { showLoginModal = true },
                                colors = ButtonDefaults.buttonColors(containerColor = HarvestGreenPrimary),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.testTag("desktop_farmer_login_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Login",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Farmer Login", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // ─────────────────────────────────────────────────────────────
            // 2. DEMO DATA COMPLIANCE NOTICE BANNER (Section 6 requirement)
            // ─────────────────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFEF3C7))
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 1280.dp)
                        .padding(horizontal = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Notice",
                        tint = Color(0xFFB45309),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Demo Data: Displaying simulated APMC mandi records. Connect Agmarknet API for live real-time auction feeds.",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF92400E)
                    )
                }
            }

            // ─────────────────────────────────────────────────────────────
            // 3. MAIN DESKTOP CONTENT CONTAINER (Max-width 1280dp)
            // ─────────────────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 1280.dp),
                    verticalArrangement = Arrangement.spacedBy(28.dp)
                ) {

                    // HERO SEARCH SECTION
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth().testTag("desktop_hero_search_card")
                    ) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            Text(
                                text = "Smart Crop Price Search & Mandi Locator",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF111827)
                            )
                            Text(
                                text = "Search wholesale mandi spot prices for vegetables, fruits, grains and pulses across Indian APMCs.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF4B5563),
                                modifier = Modifier.padding(top = 4.dp, bottom = 18.dp)
                            )

                            // Search bar with suggestions & mic
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedTextField(
                                    value = query,
                                    onValueChange = { viewModel.setSearchQuery(it) },
                                    placeholder = {
                                        Text("Search tomato, onion, wheat, rice, mango, potato...", color = Color.Gray)
                                    },
                                    leadingIcon = {
                                        Icon(Icons.Default.Search, contentDescription = "Search", tint = HarvestGreenPrimary)
                                    },
                                    trailingIcon = {
                                        IconButton(onClick = { viewModel.setSearchQuery("Tomato") }) {
                                            Icon(Icons.Default.Mic, contentDescription = "Voice", tint = HarvestGreenPrimary)
                                        }
                                    },
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = HarvestGreenPrimary,
                                        unfocusedBorderColor = Color(0xFFE5E7EB)
                                    ),
                                    modifier = Modifier.weight(1f).testTag("web_search_input")
                                )

                                Button(
                                    onClick = { currentWebSection = "prices" },
                                    colors = ButtonDefaults.buttonColors(containerColor = HarvestGreenPrimary),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.height(52.dp)
                                ) {
                                    Text("Search Prices", fontWeight = FontWeight.Bold)
                                }
                            }

                            // Quick Suggestions
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Popular:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6B7280))
                                listOf("Tomato", "Onion", "Potato", "Wheat", "Rice", "Mango", "Banana", "Chilli").forEach { crop ->
                                    Surface(
                                        shape = RoundedCornerShape(16.dp),
                                        color = Color(0xFFF3F4F6),
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(16.dp))
                                            .clickable { viewModel.setSearchQuery(crop) }
                                    ) {
                                        Text(
                                            text = crop,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color(0xFF374151),
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            // Location Hierarchical Selector: State -> District -> Mandi
                            LocationSelectorCard(
                                selectedState = selectedState,
                                selectedDistrict = selectedDistrict,
                                selectedMarket = selectedMarket,
                                availableStates = availableStates,
                                availableDistricts = availableDistricts,
                                availableMarkets = availableMarkets,
                                onSelectLocation = { s, d, m -> viewModel.setLocation(s, d, m) },
                                onClearLocation = { viewModel.clearLocationFilters() }
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Category Selector Chips Row (All Crops, Vegetables, Fruits, Grains, Pulses)
                            CategorySelectorRow(
                                selectedCategory = selectedCategory,
                                onCategorySelected = { viewModel.setCategory(it) },
                                language = language
                            )
                        }
                    }

                    // 4. TODAY'S TOP PRICES SECTION
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(SunGoldContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.EmojiEvents, contentDescription = "Top", tint = Color(0xFFB45309), modifier = Modifier.size(20.dp))
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = LocalizationManager.getText("today_top_prices", language),
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Black,
                                        color = Color(0xFF111827)
                                    )
                                    Text(
                                        text = "Top performing wholesale crops across reporting mandis",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFF6B7280)
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFF3F4F6)
                            ) {
                                Text(
                                    text = "Unit: ${unit.label}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF4B5563),
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // 4-Card Horizontal Grid for Top Prices
                        val topPrices = allPrices.sortedByDescending { it.avgPrice }.take(4)
                        Row(
                            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            topPrices.forEach { crop ->
                                DesktopTopPriceCard(
                                    crop = crop,
                                    unit = unit,
                                    language = language,
                                    onClick = { selectedCropForModal = crop }
                                )
                            }
                        }
                    }

                    // 5. CURRENT MARKET PRICES (Table & Cards Views)
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Current Market Prices",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF111827)
                                )
                                Text(
                                    text = "Real-time spot rates from verified mandis (${filteredPrices.size} records found)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF6B7280)
                                )
                            }

                            // View Toggle (Table vs Cards) & Sort options
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                // Table / Cards switch
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFFE5E7EB)
                                ) {
                                    Row(modifier = Modifier.padding(2.dp)) {
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = if (viewMode == "table") Color.White else Color.Transparent,
                                            modifier = Modifier.clip(RoundedCornerShape(6.dp)).clickable { viewMode = "table" }
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(Icons.Default.TableChart, contentDescription = "Table", modifier = Modifier.size(16.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("Table View", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }

                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = if (viewMode == "cards") Color.White else Color.Transparent,
                                            modifier = Modifier.clip(RoundedCornerShape(6.dp)).clickable { viewMode = "cards" }
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(Icons.Default.GridView, contentDescription = "Cards", modifier = Modifier.size(16.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("Cards View", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        if (viewMode == "table") {
                            // RESPONSIVE DESKTOP DATA TABLE
                            DesktopPriceDataTable(
                                cropPrices = filteredPrices,
                                unit = unit,
                                language = language,
                                onSelectCrop = { selectedCropForModal = it }
                            )
                        } else {
                            // CARDS GRID
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                filteredPrices.chunked(2).forEach { rowCrops ->
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                        rowCrops.forEach { crop ->
                                            Box(modifier = Modifier.weight(1f)) {
                                                CropPriceCard(
                                                    cropPrice = crop,
                                                    unit = unit,
                                                    language = language,
                                                    isFavorite = false,
                                                    onToggleFavorite = { viewModel.toggleFavorite(crop) },
                                                    onShare = { shareText(viewModel.formatShareText(crop)) },
                                                    onCompare = { viewModel.setCompareCrop(crop.cropName) },
                                                    onViewTrends = { viewModel.setTrendCrop(crop.cropName) },
                                                    onClick = { selectedCropForModal = crop }
                                                )
                                            }
                                        }
                                        if (rowCrops.size == 1) {
                                            Spacer(modifier = Modifier.weight(1f))
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // 6. INTERACTIVE PRICE TRENDS & CHARTS (7d, 30d, 90d)
                    Column(modifier = Modifier.fillMaxWidth()) {
                        PriceTrendSection(
                            cropName = trendCropName,
                            timeframe = trendTimeframe,
                            points = viewModel.getTrendPointsForCurrentCrop(),
                            unit = unit,
                            language = language,
                            onSelectTimeframe = { viewModel.setTrendTimeframe(it) }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Crop Switcher row
                        Row(
                            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Compare Trends:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6B7280))
                            uniqueCropNames.forEach { name ->
                                val isSelected = name.equals(trendCropName, ignoreCase = true)
                                OutlinedButton(
                                    onClick = { viewModel.setTrendCrop(name) },
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = LocalizationManager.getLocalizedCropName(name, language),
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) HarvestGreenPrimary else Color(0xFF4B5563)
                                    )
                                }
                            }
                        }
                    }

                    // 7. COMPARE MARKETS (MANDI COMPARISON)
                    MarketComparisonView(
                        cropName = compareCropName,
                        allCropPrices = allPrices,
                        availableCropNames = uniqueCropNames,
                        onSelectCrop = { viewModel.setCompareCrop(it) },
                        unit = unit,
                        language = language
                    )

                    // 8. MARKET LOCATIONS APMC DIRECTORY
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier.fillMaxWidth().testTag("market_locations_directory")
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Domain, contentDescription = "APMC", tint = HarvestGreenPrimary, modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Regulated Market Locations (APMC Mandis)",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF111827)
                                )
                            }
                            Text(
                                text = "Major agricultural terminals reporting daily auction price movements.",
                                fontSize = 12.sp,
                                color = Color(0xFF6B7280),
                                modifier = Modifier.padding(top = 2.dp, bottom = 14.dp)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                listOf(
                                    "Karnataka" to listOf("Yeshwanthpur APMC (Bengaluru)", "Kolar APMC Mandi", "Davanagere APMC"),
                                    "Maharashtra" to listOf("Lasalgaon APMC (Nashik)", "Pune Gultekdi Market", "Vashi APMC (Navi Mumbai)"),
                                    "Andhra Pradesh" to listOf("Guntur Mirchi Yard", "Rajahmundry APMC", "Tadipatri Mandi"),
                                    "Uttar Pradesh" to listOf("Agra Fatehabad Mandi", "Kanpur Mandi", "Varanasi APMC"),
                                    "Madhya Pradesh" to listOf("Sehore Sharbati Mandi", "Indore Choithram Mandi", "Mandsaur APMC")
                                ).forEach { (stateName, mandis) ->
                                    Card(
                                        shape = RoundedCornerShape(12.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB)),
                                        modifier = Modifier.width(260.dp)
                                    ) {
                                        Column(modifier = Modifier.padding(14.dp)) {
                                            Text(stateName, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = HarvestGreenPrimary)
                                            Spacer(modifier = Modifier.height(6.dp))
                                            mandis.forEach { m ->
                                                Text(
                                                    text = "• $m",
                                                    fontSize = 12.sp,
                                                    color = Color(0xFF374151),
                                                    modifier = Modifier.padding(vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // 9. ABOUT US & MISSION
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF134E24)),
                        modifier = Modifier.fillMaxWidth().testTag("about_us_section")
                    ) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            Text(
                                text = "About Kissan Adda",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                            Text(
                                text = "Kissan Adda empowers Indian farmers with clear, transparent commodity prices across regional mandis. Built with open Agmarknet integration architecture, multilingual accessibility (English, Hindi, Telugu), and smart comparison tools so farmers can sell at the right price.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFFD6F5DC),
                                modifier = Modifier.padding(top = 6.dp, bottom = 16.dp)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "National Farmer Helpline: 1800-180-1551 (Toll Free)",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SunGoldContainer
                                )
                                Text(
                                    text = "support@kissanadda.in",
                                    fontSize = 13.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    // 10. WEB FOOTER
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Divider(color = Color(0xFFE5E7EB))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "© 2026 Kissan Adda – Smart Crop Price Information System. Open Data for Farmers across India.",
                            fontSize = 12.sp,
                            color = Color(0xFF9CA3AF)
                        )
                    }
                }
            }
        }
    }

    // Language Dialog
    if (showLanguageDialog) {
        LanguageDialog(
            currentLanguage = language,
            onSelectLanguage = { viewModel.setLanguage(it) },
            onDismiss = { showLanguageDialog = false }
        )
    }

    // Farmer Login Modal
    if (showLoginModal) {
        AlertDialog(
            onDismissRequest = { showLoginModal = false },
            title = {
                Text("Farmer Login / Profile", fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Enter mobile number to view personalized crop alerts and saved mandis:")
                    OutlinedTextField(
                        value = "9876543210",
                        onValueChange = {},
                        label = { Text("Mobile Number (+91)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Surface(
                        color = HarvestGreenContainer,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "✓ Logged in as Ramesh Patel (Verified Farmer ID #KP-8492)",
                            color = HarvestGreenPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showLoginModal = false },
                    colors = ButtonDefaults.buttonColors(containerColor = HarvestGreenPrimary)
                ) {
                    Text("Done")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLoginModal = false }) {
                    Text("Close")
                }
            }
        )
    }

    // Crop Details Modal
    selectedCropForModal?.let { crop ->
        val localizedName = LocalizationManager.getLocalizedCropName(crop.cropName, language)
        val unitLabel = if (unit == PriceUnit.PER_KG) "kg" else "qtl"
        AlertDialog(
            onDismissRequest = { selectedCropForModal = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🌾 $localizedName Details", fontWeight = FontWeight.Black)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Mandi: ${crop.marketName} (${crop.district}, ${crop.state})",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )
                    Divider()
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Min Price", fontSize = 11.sp, color = Color.Gray)
                            Text("₹%.1f/$unitLabel".format(crop.getDisplayMin(unit)), fontWeight = FontWeight.Bold)
                        }
                        Column {
                            Text("Modal / Avg Price", fontSize = 11.sp, color = HarvestGreenPrimary, fontWeight = FontWeight.Bold)
                            Text("₹%.1f/$unitLabel".format(crop.getDisplayAvg(unit)), fontWeight = FontWeight.Black, color = HarvestGreenPrimary, fontSize = 16.sp)
                        }
                        Column {
                            Text("Max Price", fontSize = 11.sp, color = Color.Gray)
                            Text("₹%.1f/$unitLabel".format(crop.getDisplayMax(unit)), fontWeight = FontWeight.Bold)
                        }
                    }
                    Divider()
                    Text("Price Movement: %+.1f%% compared to yesterday".format(crop.priceChangePercent), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Text("Last Updated: ${crop.lastUpdated}", fontSize = 12.sp, color = Color.Gray)
                    Surface(
                        color = Color(0xFFF3F4F6),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Unit Conversion: ₹%.1f/kg = ₹%.0f/quintal (100 kg)".format(crop.avgPrice, crop.avgPrice * 100),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF374151),
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        shareText(viewModel.formatShareText(crop))
                        selectedCropForModal = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = HarvestGreenPrimary)
                ) {
                    Text("Share Rate")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedCropForModal = null }) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
fun WebNavLink(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Text(
        text = title,
        fontSize = 13.sp,
        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
        color = if (isSelected) HarvestGreenPrimary else Color(0xFF4B5563),
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp)
    )
}

@Composable
fun DesktopTopPriceCard(
    crop: CropPrice,
    unit: PriceUnit,
    language: AppLanguage,
    onClick: () -> Unit
) {
    val unitStr = if (unit == PriceUnit.PER_KG) "kg" else "qtl"
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .width(260.dp)
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = LocalizationManager.getLocalizedCropName(crop.cropName, language),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )
                    Text(
                        text = "${crop.marketName} • ${crop.district}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF6B7280)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (crop.isPriceUp) Color(0xFFDCFCE7) else Color(0xFFFEE2E2)
                ) {
                    Text(
                        text = "${if (crop.isPriceUp) "↑" else "↓"} %+.1f%%".format(crop.priceChangePercent),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (crop.isPriceUp) Color(0xFF16A34A) else Color(0xFFDC2626),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text("Modal Price", fontSize = 10.sp, color = Color(0xFF9CA3AF), fontWeight = FontWeight.Bold)
                    Text(
                        text = "₹%.1f/$unitStr".format(crop.getDisplayAvg(unit)),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = HarvestGreenPrimary
                    )
                }

                Text(
                    text = "View details →",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = HarvestGreenPrimary
                )
            }
        }
    }
}

@Composable
fun DesktopPriceDataTable(
    cropPrices: List<CropPrice>,
    unit: PriceUnit,
    language: AppLanguage,
    onSelectCrop: (CropPrice) -> Unit
) {
    val unitStr = if (unit == PriceUnit.PER_KG) "kg" else "qtl"

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth().testTag("desktop_data_table")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Table Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF3F4F6))
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Commodity / Crop", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF374151), modifier = Modifier.weight(1.5f))
                Text("Mandi / Location", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF374151), modifier = Modifier.weight(1.5f))
                Text("Min Price", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF374151), modifier = Modifier.weight(0.9f))
                Text("Max Price", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF374151), modifier = Modifier.weight(0.9f))
                Text("Modal Avg ($unitStr)", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = HarvestGreenPrimary, modifier = Modifier.weight(1.1f))
                Text("24h Change", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF374151), modifier = Modifier.weight(1f))
                Text("Action", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF374151), modifier = Modifier.weight(0.8f))
            }

            Spacer(modifier = Modifier.height(6.dp))

            cropPrices.forEachIndexed { index, crop ->
                val isUp = crop.isPriceUp

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .clickable { onSelectCrop(crop) }
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Commodity
                    Row(modifier = Modifier.weight(1.5f), verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = LocalizationManager.getLocalizedCropName(crop.cropName, language),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color(0xFF111827)
                        )
                    }

                    // Mandi & Location
                    Column(modifier = Modifier.weight(1.5f)) {
                        Text(crop.marketName, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Color(0xFF1F2937))
                        Text("${crop.district}, ${crop.state}", fontSize = 11.sp, color = Color(0xFF6B7280))
                    }

                    // Min
                    Text("₹%.1f".format(crop.getDisplayMin(unit)), fontSize = 13.sp, color = Color(0xFF4B5563), modifier = Modifier.weight(0.9f))

                    // Max
                    Text("₹%.1f".format(crop.getDisplayMax(unit)), fontSize = 13.sp, color = Color(0xFF4B5563), modifier = Modifier.weight(0.9f))

                    // Modal Avg
                    Text(
                        text = "₹%.1f".format(crop.getDisplayAvg(unit)),
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp,
                        color = HarvestGreenPrimary,
                        modifier = Modifier.weight(1.1f)
                    )

                    // 24h Change
                    Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isUp) Color(0xFFDCFCE7) else Color(0xFFFEE2E2)
                        ) {
                            Text(
                                text = "${if (isUp) "↑" else "↓"} %+.1f%%".format(crop.priceChangePercent),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isUp) Color(0xFF16A34A) else Color(0xFFDC2626),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    // Action button
                    TextButton(
                        onClick = { onSelectCrop(crop) },
                        modifier = Modifier.weight(0.8f)
                    ) {
                        Text("Details", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = HarvestGreenPrimary)
                    }
                }

                if (index < cropPrices.size - 1) {
                    Divider(color = Color(0xFFF3F4F6), thickness = 1.dp)
                }
            }
        }
    }
}
