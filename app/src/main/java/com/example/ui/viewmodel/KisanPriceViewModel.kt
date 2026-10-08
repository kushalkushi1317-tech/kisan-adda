package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.ChatMessage
import com.example.ai.KisanAiAssistant
import com.example.data.local.KisanPriceDatabase
import com.example.data.local.SampleData
import com.example.data.model.CropPrice
import com.example.data.model.FavoriteItem
import com.example.data.model.MyCrop
import com.example.data.model.PriceAlert
import com.example.data.model.PriceTrendPoint
import com.example.data.model.PriceUnit
import com.example.data.repository.KisanPriceRepository
import com.example.localization.AppLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class SortOption {
    HIGHEST_PRICE,
    LOWEST_PRICE,
    LATEST_UPDATE,
    PRICE_INCREASE
}

data class MarketFilterState(
    val query: String = "",
    val category: String = "All",
    val state: String? = null,
    val district: String? = null,
    val market: String? = null,
    val sortBy: SortOption = SortOption.HIGHEST_PRICE
)

class KisanPriceViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: KisanPriceRepository

    init {
        val db = KisanPriceDatabase.getDatabase(application)
        repository = KisanPriceRepository(
            cropPriceDao = db.cropPriceDao(),
            favoriteDao = db.favoriteDao(),
            myCropDao = db.myCropDao(),
            priceAlertDao = db.priceAlertDao()
        )
        viewModelScope.launch {
            repository.initializeIfEmpty()
        }
    }

    // Repository flows
    val allPrices: StateFlow<List<CropPrice>> = repository.allPrices
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favorites: StateFlow<List<FavoriteItem>> = repository.allFavorites
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val myCrops: StateFlow<List<MyCrop>> = repository.allMyCrops
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val priceAlerts: StateFlow<List<PriceAlert>> = repository.allAlerts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // UI State variables
    private val _selectedLanguage = MutableStateFlow(AppLanguage.ENGLISH)
    val selectedLanguage: StateFlow<AppLanguage> = _selectedLanguage.asStateFlow()

    private val _selectedUnit = MutableStateFlow(PriceUnit.PER_KG)
    val selectedUnit: StateFlow<PriceUnit> = _selectedUnit.asStateFlow()

    private val _filters = MutableStateFlow(MarketFilterState())
    val filters: StateFlow<MarketFilterState> = _filters.asStateFlow()

    val searchQuery: StateFlow<String> = _filters.map { it.query }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val selectedCategory: StateFlow<String> = _filters.map { it.category }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "All")

    val selectedState: StateFlow<String?> = _filters.map { it.state }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val selectedDistrict: StateFlow<String?> = _filters.map { it.district }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val selectedMarket: StateFlow<String?> = _filters.map { it.market }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val sortBy: StateFlow<SortOption> = _filters.map { it.sortBy }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SortOption.HIGHEST_PRICE)

    private val _selectedDetailCrop = MutableStateFlow<CropPrice?>(null)
    val selectedDetailCrop: StateFlow<CropPrice?> = _selectedDetailCrop.asStateFlow()

    private val _trendCropName = MutableStateFlow("Tomato")
    val trendCropName: StateFlow<String> = _trendCropName.asStateFlow()

    private val _trendTimeframe = MutableStateFlow("7 Days")
    val trendTimeframe: StateFlow<String> = _trendTimeframe.asStateFlow()

    private val _compareCropName = MutableStateFlow("Tomato")
    val compareCropName: StateFlow<String> = _compareCropName.asStateFlow()

    private val _isAdminMode = MutableStateFlow(false)
    val isAdminMode: StateFlow<Boolean> = _isAdminMode.asStateFlow()

    private val _chatMessages = MutableStateFlow(
        listOf(
            ChatMessage(
                text = "Namaste! I am Kisan Mitra, your AI Market Assistant. Ask me today's prices, best mandis to sell, or market trends!",
                isUser = false
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _feedbackMessage = MutableStateFlow<String?>(null)
    val feedbackMessage: StateFlow<String?> = _feedbackMessage.asStateFlow()

    // Filtered Crop Prices
    val filteredPrices: StateFlow<List<CropPrice>> = combine(allPrices, _filters) { prices, f ->
        var list = prices

        if (f.query.isNotBlank()) {
            val q = f.query.trim().lowercase(Locale.ROOT)
            list = list.filter {
                it.cropName.lowercase(Locale.ROOT).contains(q) ||
                it.marketName.lowercase(Locale.ROOT).contains(q) ||
                it.district.lowercase(Locale.ROOT).contains(q) ||
                it.state.lowercase(Locale.ROOT).contains(q)
            }
        }

        if (f.category != "All") {
            list = list.filter { it.category.equals(f.category, ignoreCase = true) }
        }

        if (!f.state.isNullOrBlank()) {
            list = list.filter { it.state.equals(f.state, ignoreCase = true) }
        }

        if (!f.district.isNullOrBlank()) {
            list = list.filter { it.district.equals(f.district, ignoreCase = true) }
        }

        if (!f.market.isNullOrBlank()) {
            list = list.filter { it.marketName.equals(f.market, ignoreCase = true) }
        }

        when (f.sortBy) {
            SortOption.HIGHEST_PRICE -> list.sortedByDescending { it.avgPrice }
            SortOption.LOWEST_PRICE -> list.sortedBy { it.avgPrice }
            SortOption.LATEST_UPDATE -> list.sortedByDescending { it.timestamp }
            SortOption.PRICE_INCREASE -> list.sortedByDescending { it.priceChangePercent }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Unique filter options
    val availableStates: StateFlow<List<String>> = allPrices.map { prices ->
        prices.map { it.state }.distinct().sorted()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val availableDistricts: StateFlow<List<String>> = combine(allPrices, selectedState) { prices, state ->
        val filtered = if (state.isNullOrBlank()) prices else prices.filter { it.state == state }
        filtered.map { it.district }.distinct().sorted()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val availableMarkets: StateFlow<List<String>> = combine(allPrices, selectedDistrict) { prices, dist ->
        val filtered = if (dist.isNullOrBlank()) prices else prices.filter { it.district == dist }
        filtered.map { it.marketName }.distinct().sorted()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val uniqueCropNames: StateFlow<List<String>> = allPrices.map { prices ->
        prices.map { it.cropName }.distinct().sorted()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), listOf("Tomato", "Onion", "Potato"))

    // Actions
    fun setLanguage(language: AppLanguage) {
        _selectedLanguage.value = language
    }

    fun toggleUnit() {
        _selectedUnit.value = if (_selectedUnit.value == PriceUnit.PER_KG) PriceUnit.PER_QUINTAL else PriceUnit.PER_KG
    }

    fun setUnit(unit: PriceUnit) {
        _selectedUnit.value = unit
    }

    fun setSearchQuery(query: String) {
        _filters.update { it.copy(query = query) }
    }

    fun setCategory(category: String) {
        _filters.update { it.copy(category = category) }
    }

    fun setLocation(state: String?, district: String? = null, market: String? = null) {
        _filters.update { it.copy(state = state, district = district, market = market) }
    }

    fun clearLocationFilters() {
        _filters.update { it.copy(state = null, district = null, market = null) }
    }

    fun setSortBy(option: SortOption) {
        _filters.update { it.copy(sortBy = option) }
    }

    fun selectCropDetail(crop: CropPrice?) {
        _selectedDetailCrop.value = crop
        if (crop != null) {
            _trendCropName.value = crop.cropName
            _compareCropName.value = crop.cropName
        }
    }

    fun setTrendCrop(cropName: String) {
        _trendCropName.value = cropName
    }

    fun setTrendTimeframe(timeframe: String) {
        _trendTimeframe.value = timeframe
    }

    fun setCompareCrop(cropName: String) {
        _compareCropName.value = cropName
    }

    fun toggleFavorite(crop: CropPrice) {
        viewModelScope.launch {
            repository.toggleFavorite(crop.id, crop.cropName, crop.marketName)
            _feedbackMessage.value = "Updated favorites for ${crop.cropName}"
        }
    }

    fun addMyCrop(cropName: String, quantity: String, targetPrice: Double, notes: String) {
        viewModelScope.launch {
            repository.addMyCrop(MyCrop(cropName = cropName, quantityAcreOrBags = quantity, targetPricePerKg = targetPrice, notes = notes))
            _feedbackMessage.value = "Added $cropName to My Crops"
        }
    }

    fun deleteMyCrop(crop: MyCrop) {
        viewModelScope.launch {
            repository.deleteMyCrop(crop)
            _feedbackMessage.value = "Removed ${crop.cropName} from My Crops"
        }
    }

    fun addPriceAlert(cropName: String, targetPrice: Double, alertWhenAbove: Boolean) {
        viewModelScope.launch {
            repository.addPriceAlert(PriceAlert(cropName = cropName, targetPricePerKg = targetPrice, alertWhenAbove = alertWhenAbove))
            _feedbackMessage.value = "Alert set for $cropName at ₹$targetPrice/kg"
        }
    }

    fun deletePriceAlert(alert: PriceAlert) {
        viewModelScope.launch {
            repository.deletePriceAlert(alert)
        }
    }

    fun toggleAdminMode() {
        _isAdminMode.value = !_isAdminMode.value
    }

    fun sendChatMessage(query: String) {
        val userMsg = ChatMessage(text = query, isUser = true)
        val currentList = _chatMessages.value.toMutableList()
        currentList.add(userMsg)
        _chatMessages.value = currentList

        viewModelScope.launch {
            val responseText = KisanAiAssistant.answerQuery(
                query = query,
                cropPrices = allPrices.value,
                unit = _selectedUnit.value,
                language = _selectedLanguage.value
            )
            val aiMsg = ChatMessage(text = responseText, isUser = false)
            val updatedList = _chatMessages.value.toMutableList()
            updatedList.add(aiMsg)
            _chatMessages.value = updatedList
        }
    }

    fun addNewCropPrice(
        cropName: String,
        category: String,
        state: String,
        district: String,
        market: String,
        minPrice: Double,
        maxPrice: Double,
        avgPrice: Double
    ) {
        viewModelScope.launch {
            val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
            val formattedTime = sdf.format(Date())
            val newPrice = CropPrice(
                cropName = cropName.trim(),
                category = category,
                state = state.trim(),
                district = district.trim(),
                marketName = market.trim(),
                minPrice = minPrice,
                maxPrice = maxPrice,
                avgPrice = avgPrice,
                priceChangePercent = 0.0,
                lastUpdated = "Today, $formattedTime",
                isSampleData = false
            )
            repository.addCropPrice(newPrice)
            _feedbackMessage.value = "Added $cropName at $market successfully"
        }
    }

    fun updateCropPrice(cropPrice: CropPrice) {
        viewModelScope.launch {
            repository.updateCropPrice(cropPrice)
            _feedbackMessage.value = "Updated ${cropPrice.cropName} price successfully"
        }
    }

    fun deleteCropPrice(cropPrice: CropPrice) {
        viewModelScope.launch {
            repository.deleteCropPrice(cropPrice)
            _feedbackMessage.value = "Deleted record for ${cropPrice.cropName}"
        }
    }

    fun resetToSampleData() {
        viewModelScope.launch {
            repository.resetToDefaultData()
            _feedbackMessage.value = "Reset to default mandi dataset"
        }
    }

    fun clearFeedback() {
        _feedbackMessage.value = null
    }

    fun getTrendPointsForCurrentCrop(): List<PriceTrendPoint> {
        val cropName = _trendCropName.value
        val baseAvg = allPrices.value.firstOrNull { it.cropName.equals(cropName, ignoreCase = true) }?.avgPrice ?: 30.0
        val trends = SampleData.getTrendHistoryForCrop(cropName, baseAvg)
        return trends[_trendTimeframe.value] ?: trends["7 Days"] ?: emptyList()
    }

    fun formatShareText(crop: CropPrice): String {
        val u = if (_selectedUnit.value == PriceUnit.PER_KG) "kg" else "quintal"
        val min = crop.getDisplayMin(_selectedUnit.value)
        val max = crop.getDisplayMax(_selectedUnit.value)
        val avg = crop.getDisplayAvg(_selectedUnit.value)
        val direction = if (crop.isPriceUp) "↑ +%.1f%%".format(crop.priceChangePercent) else "↓ %.1f%%".format(crop.priceChangePercent)
        return """
🌾 *Kisan Price Mandi Update* 🌾
━━━━━━━━━━━━━━━━━━━━
Crop: *${crop.cropName}* (${crop.category})
Market: *${crop.marketName}*
District: ${crop.district}, ${crop.state}

💰 *Today's Rates ($u):*
• Min Price: ₹%.1f
• Max Price: ₹%.1f
• Average: *₹%.1f* ($direction)

🕒 Updated: ${crop.lastUpdated}
━━━━━━━━━━━━━━━━━━━━
Shared via Kisan Price App
_Know Today's Price. Sell at the Right Price._
        """.trimIndent().format(min, max, avg)
    }

    fun formatFullReportText(): String {
        val list = filteredPrices.value.take(10)
        val sb = StringBuilder()
        sb.append("🌾 *Kisan Price - Mandi Market Report* 🌾\n")
        sb.append("Tagline: Know Today's Price. Sell at the Right Price.\n")
        sb.append("Date: ${SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date())}\n")
        sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n")

        for (item in list) {
            val u = if (_selectedUnit.value == PriceUnit.PER_KG) "kg" else "qtl"
            sb.append("• *${item.cropName}* (${item.marketName}):\n")
            sb.append("  Avg: ₹%.1f/%s | Min: ₹%.1f | Max: ₹%.1f | (%+.1f%%)\n\n".format(
                item.getDisplayAvg(_selectedUnit.value),
                u,
                item.getDisplayMin(_selectedUnit.value),
                item.getDisplayMax(_selectedUnit.value),
                item.priceChangePercent
            ))
        }
        sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n")
        sb.append("Generated with Kisan Price. Real-time simulated Agmarknet dataset.")
        return sb.toString()
    }
}
