package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.CropPrice
import com.example.data.model.PriceUnit
import com.example.localization.AppLanguage
import com.example.localization.LocalizationManager
import com.example.ui.components.AdminDashboardView
import com.example.ui.components.AiAssistantView
import com.example.ui.components.CategorySelectorRow
import com.example.ui.components.CropPriceCard
import com.example.ui.components.FarmerDashboardView
import com.example.ui.components.KisanTopBar
import com.example.ui.components.LanguageDialog
import com.example.ui.components.LargeSearchBar
import com.example.ui.components.LocationSelectorCard
import com.example.ui.components.MarketComparisonView
import com.example.ui.components.PriceTrendSection
import com.example.ui.theme.HarvestGreenContainer
import com.example.ui.theme.HarvestGreenPrimary
import com.example.ui.theme.SunGold
import com.example.ui.viewmodel.KisanPriceViewModel
import com.example.ui.viewmodel.SortOption

enum class AppTab(val title: String) {
    HOME("Home"),
    MARKET_PRICES("Prices"),
    TRENDS("Trends"),
    COMPARE("Compare"),
    MY_CROPS("My Crops"),
    AI_ASSISTANT("AI Assistant")
}

@Composable
fun MainAppScreen(
    viewModel: KisanPriceViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    var currentTab by remember { mutableStateOf(AppTab.HOME) }
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showReportDialog by remember { mutableStateOf(false) }

    // Collect ViewModel states
    val language by viewModel.selectedLanguage.collectAsStateWithLifecycle()
    val unit by viewModel.selectedUnit.collectAsStateWithLifecycle()
    val isAdminMode by viewModel.isAdminMode.collectAsStateWithLifecycle()

    val query by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val selectedState by viewModel.selectedState.collectAsStateWithLifecycle()
    val selectedDistrict by viewModel.selectedDistrict.collectAsStateWithLifecycle()
    val selectedMarket by viewModel.selectedMarket.collectAsStateWithLifecycle()
    val sortBy by viewModel.sortBy.collectAsStateWithLifecycle()

    val filteredPrices by viewModel.filteredPrices.collectAsStateWithLifecycle()
    val allPrices by viewModel.allPrices.collectAsStateWithLifecycle()
    val favorites by viewModel.favorites.collectAsStateWithLifecycle()
    val myCrops by viewModel.myCrops.collectAsStateWithLifecycle()
    val priceAlerts by viewModel.priceAlerts.collectAsStateWithLifecycle()
    val chatMessages by viewModel.chatMessages.collectAsStateWithLifecycle()

    val availableStates by viewModel.availableStates.collectAsStateWithLifecycle()
    val availableDistricts by viewModel.availableDistricts.collectAsStateWithLifecycle()
    val availableMarkets by viewModel.availableMarkets.collectAsStateWithLifecycle()
    val uniqueCropNames by viewModel.uniqueCropNames.collectAsStateWithLifecycle()

    val trendCropName by viewModel.trendCropName.collectAsStateWithLifecycle()
    val trendTimeframe by viewModel.trendTimeframe.collectAsStateWithLifecycle()
    val compareCropName by viewModel.compareCropName.collectAsStateWithLifecycle()

    val feedbackMsg by viewModel.feedbackMessage.collectAsStateWithLifecycle()

    val favoriteIds = remember(favorites) { favorites.map { it.cropId }.toSet() }

    LaunchedEffect(feedbackMsg) {
        feedbackMsg?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearFeedback()
        }
    }

    fun shareText(text: String, title: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, title)
        context.startActivity(shareIntent)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            KisanTopBar(
                selectedLanguage = language,
                selectedUnit = unit,
                isAdminMode = isAdminMode,
                onOpenLanguageDialog = { showLanguageDialog = true },
                onToggleUnit = { viewModel.toggleUnit() },
                onToggleAdminMode = { viewModel.toggleAdminMode() }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp,
                modifier = Modifier.testTag("main_bottom_nav_bar")
            ) {
                NavigationBarItem(
                    selected = currentTab == AppTab.HOME && !isAdminMode,
                    onClick = {
                        if (isAdminMode) viewModel.toggleAdminMode()
                        currentTab = AppTab.HOME
                    },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = HarvestGreenPrimary,
                        selectedTextColor = HarvestGreenPrimary,
                        indicatorColor = HarvestGreenContainer
                    ),
                    modifier = Modifier.testTag("nav_item_home")
                )
                NavigationBarItem(
                    selected = currentTab == AppTab.MARKET_PRICES && !isAdminMode,
                    onClick = {
                        if (isAdminMode) viewModel.toggleAdminMode()
                        currentTab = AppTab.MARKET_PRICES
                    },
                    icon = { Icon(Icons.Default.Storefront, contentDescription = "Prices") },
                    label = { Text("Prices", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = HarvestGreenPrimary,
                        selectedTextColor = HarvestGreenPrimary,
                        indicatorColor = HarvestGreenContainer
                    ),
                    modifier = Modifier.testTag("nav_item_prices")
                )
                NavigationBarItem(
                    selected = currentTab == AppTab.TRENDS && !isAdminMode,
                    onClick = {
                        if (isAdminMode) viewModel.toggleAdminMode()
                        currentTab = AppTab.TRENDS
                    },
                    icon = { Icon(Icons.Default.ShowChart, contentDescription = "Trends") },
                    label = { Text("Trends", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = HarvestGreenPrimary,
                        selectedTextColor = HarvestGreenPrimary,
                        indicatorColor = HarvestGreenContainer
                    ),
                    modifier = Modifier.testTag("nav_item_trends")
                )
                NavigationBarItem(
                    selected = currentTab == AppTab.COMPARE && !isAdminMode,
                    onClick = {
                        if (isAdminMode) viewModel.toggleAdminMode()
                        currentTab = AppTab.COMPARE
                    },
                    icon = { Icon(Icons.Default.CompareArrows, contentDescription = "Compare") },
                    label = { Text("Compare", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = HarvestGreenPrimary,
                        selectedTextColor = HarvestGreenPrimary,
                        indicatorColor = HarvestGreenContainer
                    ),
                    modifier = Modifier.testTag("nav_item_compare")
                )
                NavigationBarItem(
                    selected = currentTab == AppTab.MY_CROPS && !isAdminMode,
                    onClick = {
                        if (isAdminMode) viewModel.toggleAdminMode()
                        currentTab = AppTab.MY_CROPS
                    },
                    icon = { Icon(Icons.Default.Agriculture, contentDescription = "My Crops") },
                    label = { Text("My Crops", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = HarvestGreenPrimary,
                        selectedTextColor = HarvestGreenPrimary,
                        indicatorColor = HarvestGreenContainer
                    ),
                    modifier = Modifier.testTag("nav_item_my_crops")
                )
                NavigationBarItem(
                    selected = currentTab == AppTab.AI_ASSISTANT && !isAdminMode,
                    onClick = {
                        if (isAdminMode) viewModel.toggleAdminMode()
                        currentTab = AppTab.AI_ASSISTANT
                    },
                    icon = { Icon(Icons.Default.SmartToy, contentDescription = "AI") },
                    label = { Text("AI Help", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = HarvestGreenPrimary,
                        selectedTextColor = HarvestGreenPrimary,
                        indicatorColor = HarvestGreenContainer
                    ),
                    modifier = Modifier.testTag("nav_item_ai")
                )
            }
        },
        floatingActionButton = {
            if (!isAdminMode && (currentTab == AppTab.HOME || currentTab == AppTab.MARKET_PRICES)) {
                FloatingActionButton(
                    onClick = { showReportDialog = true },
                    containerColor = SunGold,
                    contentColor = Color.White,
                    modifier = Modifier.testTag("fab_price_report")
                ) {
                    Icon(imageVector = Icons.Default.Description, contentDescription = "Report")
                }
            }
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF8FAF7))
        ) {
            if (isAdminMode) {
                // Admin Screen View
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    AdminDashboardView(
                        allPrices = allPrices,
                        onAddPrice = { name, cat, st, dist, mkt, min, max, avg ->
                            viewModel.addNewCropPrice(name, cat, st, dist, mkt, min, max, avg)
                        },
                        onUpdatePrice = { viewModel.updateCropPrice(it) },
                        onDeletePrice = { viewModel.deleteCropPrice(it) },
                        onResetData = { viewModel.resetToSampleData() }
                    )
                }
            } else {
                when (currentTab) {
                    AppTab.HOME -> {
                        HomeScreenContent(
                            query = query,
                            onQueryChange = { viewModel.setSearchQuery(it) },
                            selectedCategory = selectedCategory,
                            onCategorySelected = { viewModel.setCategory(it) },
                            selectedState = selectedState,
                            selectedDistrict = selectedDistrict,
                            selectedMarket = selectedMarket,
                            availableStates = availableStates,
                            availableDistricts = availableDistricts,
                            availableMarkets = availableMarkets,
                            onSelectLocation = { s, d, m -> viewModel.setLocation(s, d, m) },
                            onClearLocation = { viewModel.clearLocationFilters() },
                            cropPrices = filteredPrices,
                            favoriteCropIds = favoriteIds,
                            unit = unit,
                            language = language,
                            onToggleFavorite = { viewModel.toggleFavorite(it) },
                            onShareCrop = { shareText(viewModel.formatShareText(it), "Share Crop Rate") },
                            onCompareCrop = {
                                viewModel.setCompareCrop(it.cropName)
                                currentTab = AppTab.COMPARE
                            },
                            onViewTrends = {
                                viewModel.setTrendCrop(it.cropName)
                                currentTab = AppTab.TRENDS
                            },
                            onVoiceClick = {
                                viewModel.setSearchQuery("Tomato")
                            }
                        )
                    }

                    AppTab.MARKET_PRICES -> {
                        // Full Market Price Page (Prompt Requirement #2 & #3)
                        MarketPricesFullPage(
                            query = query,
                            onQueryChange = { viewModel.setSearchQuery(it) },
                            selectedCategory = selectedCategory,
                            onCategorySelected = { viewModel.setCategory(it) },
                            selectedState = selectedState,
                            selectedDistrict = selectedDistrict,
                            selectedMarket = selectedMarket,
                            availableStates = availableStates,
                            availableDistricts = availableDistricts,
                            availableMarkets = availableMarkets,
                            onSelectLocation = { s, d, m -> viewModel.setLocation(s, d, m) },
                            onClearLocation = { viewModel.clearLocationFilters() },
                            sortBy = sortBy,
                            onSelectSort = { viewModel.setSortBy(it) },
                            cropPrices = filteredPrices,
                            favoriteCropIds = favoriteIds,
                            unit = unit,
                            language = language,
                            onToggleFavorite = { viewModel.toggleFavorite(it) },
                            onShareCrop = { shareText(viewModel.formatShareText(it), "Share Crop Rate") },
                            onCompareCrop = {
                                viewModel.setCompareCrop(it.cropName)
                                currentTab = AppTab.COMPARE
                            },
                            onViewTrends = {
                                viewModel.setTrendCrop(it.cropName)
                                currentTab = AppTab.TRENDS
                            },
                            onVoiceClick = { viewModel.setSearchQuery("Onion") }
                        )
                    }

                    AppTab.TRENDS -> {
                        // Price Trends Deep Dive (Prompt Requirement #4)
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                                .verticalScroll(rememberScrollState())
                        ) {
                            PriceTrendSection(
                                cropName = trendCropName,
                                timeframe = trendTimeframe,
                                points = viewModel.getTrendPointsForCurrentCrop(),
                                unit = unit,
                                language = language,
                                onSelectTimeframe = { viewModel.setTrendTimeframe(it) }
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Crop Quick Switcher
                            Text(
                                text = "Select another crop for trends:",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF111827)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
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
                    }

                    AppTab.COMPARE -> {
                        // Compare Markets (Prompt Requirement #7)
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                                .verticalScroll(rememberScrollState())
                        ) {
                            MarketComparisonView(
                                cropName = compareCropName,
                                allCropPrices = allPrices,
                                availableCropNames = uniqueCropNames,
                                onSelectCrop = { viewModel.setCompareCrop(it) },
                                unit = unit,
                                language = language
                            )
                        }
                    }

                    AppTab.MY_CROPS -> {
                        // Farmer Dashboard & Price Alerts (Prompt Requirement #5 & #6)
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                                .verticalScroll(rememberScrollState())
                        ) {
                            FarmerDashboardView(
                                myCrops = myCrops,
                                allPrices = allPrices,
                                priceAlerts = priceAlerts,
                                unit = unit,
                                language = language,
                                onAddMyCrop = { name, qty, tgt, notes -> viewModel.addMyCrop(name, qty, tgt, notes) },
                                onDeleteMyCrop = { viewModel.deleteMyCrop(it) },
                                onAddAlert = { name, tgt, above -> viewModel.addPriceAlert(name, tgt, above) },
                                onDeleteAlert = { viewModel.deletePriceAlert(it) }
                            )
                        }
                    }

                    AppTab.AI_ASSISTANT -> {
                        // AI Market Assistant (Prompt Requirement #12)
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                                .verticalScroll(rememberScrollState())
                        ) {
                            AiAssistantView(
                                chatMessages = chatMessages,
                                language = language,
                                onSendMessage = { viewModel.sendChatMessage(it) },
                                onVoiceClick = { viewModel.sendChatMessage("What is today's tomato price?") }
                            )
                        }
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

    // Print / Download Price Report Dialog (Prompt Requirement #5)
    if (showReportDialog) {
        val reportText = viewModel.formatFullReportText()
        AlertDialog(
            onDismissRequest = { showReportDialog = false },
            title = {
                Text("Mandi Price Report Summary", fontWeight = FontWeight.Bold)
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = reportText,
                        fontSize = 12.sp,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                        color = Color(0xFF1E293B)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        shareText(reportText, "Share Mandi Price Report")
                        showReportDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = HarvestGreenPrimary)
                ) {
                    Text("Share / WhatsApp")
                }
            },
            dismissButton = {
                TextButton(onClick = { showReportDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
fun MarketPricesFullPage(
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
    sortBy: SortOption,
    onSelectSort: (SortOption) -> Unit,
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
    var showSortMenu by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            LargeSearchBar(
                query = query,
                onQueryChange = onQueryChange,
                language = language,
                onVoiceClick = onVoiceClick
            )
        }

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

        item {
            CategorySelectorRow(
                selectedCategory = selectedCategory,
                onCategorySelected = onCategorySelected,
                language = language
            )
        }

        // Sort Header Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${cropPrices.size} Mandi Rates Found",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )

                Box {
                    OutlinedButton(
                        onClick = { showSortMenu = true },
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Sort, contentDescription = "Sort", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = when (sortBy) {
                                SortOption.HIGHEST_PRICE -> "Highest Price"
                                SortOption.LOWEST_PRICE -> "Lowest Price"
                                SortOption.LATEST_UPDATE -> "Latest Update"
                                SortOption.PRICE_INCREASE -> "Price Increase"
                            },
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    DropdownMenu(
                        expanded = showSortMenu,
                        onDismissRequest = { showSortMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Highest Price") },
                            onClick = {
                                onSelectSort(SortOption.HIGHEST_PRICE)
                                showSortMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Lowest Price") },
                            onClick = {
                                onSelectSort(SortOption.LOWEST_PRICE)
                                showSortMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Latest Update") },
                            onClick = {
                                onSelectSort(SortOption.LATEST_UPDATE)
                                showSortMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Price Increase") },
                            onClick = {
                                onSelectSort(SortOption.PRICE_INCREASE)
                                showSortMenu = false
                            }
                        )
                    }
                }
            }
        }

        // Cards list
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
