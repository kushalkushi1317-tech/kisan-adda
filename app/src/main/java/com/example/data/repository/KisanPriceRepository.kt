package com.example.data.repository

import com.example.data.local.CropPriceDao
import com.example.data.local.FavoriteDao
import com.example.data.local.MyCropDao
import com.example.data.local.PriceAlertDao
import com.example.data.local.SampleData
import com.example.data.model.CropPrice
import com.example.data.model.FavoriteItem
import com.example.data.model.MyCrop
import com.example.data.model.PriceAlert
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class KisanPriceRepository(
    private val cropPriceDao: CropPriceDao,
    private val favoriteDao: FavoriteDao,
    private val myCropDao: MyCropDao,
    private val priceAlertDao: PriceAlertDao
) {
    val allPrices: Flow<List<CropPrice>> = cropPriceDao.getAllCropPrices()
    val allFavorites: Flow<List<FavoriteItem>> = favoriteDao.getAllFavorites()
    val allMyCrops: Flow<List<MyCrop>> = myCropDao.getAllMyCrops()
    val allAlerts: Flow<List<PriceAlert>> = priceAlertDao.getAllAlerts()

    suspend fun initializeIfEmpty() {
        if (cropPriceDao.getCount() == 0) {
            cropPriceDao.insertAll(SampleData.defaultCropPrices)
            for (crop in SampleData.defaultMyCrops) {
                myCropDao.insert(crop)
            }
            for (alert in SampleData.defaultAlerts) {
                priceAlertDao.insert(alert)
            }
        }
    }

    suspend fun addCropPrice(cropPrice: CropPrice): Long {
        return cropPriceDao.insert(cropPrice)
    }

    suspend fun updateCropPrice(cropPrice: CropPrice) {
        cropPriceDao.update(cropPrice)
    }

    suspend fun deleteCropPrice(cropPrice: CropPrice) {
        cropPriceDao.delete(cropPrice)
    }

    suspend fun resetToDefaultData() {
        cropPriceDao.deleteAll()
        cropPriceDao.insertAll(SampleData.defaultCropPrices)
    }

    suspend fun toggleFavorite(cropId: Long, cropName: String, marketName: String) {
        val isFav = favoriteDao.isFavorite(cropId).firstOrNull() ?: false
        if (isFav) {
            favoriteDao.deleteById(cropId)
        } else {
            favoriteDao.insert(FavoriteItem(cropId, cropName, marketName))
        }
    }

    suspend fun addMyCrop(crop: MyCrop): Long {
        return myCropDao.insert(crop)
    }

    suspend fun deleteMyCrop(crop: MyCrop) {
        myCropDao.delete(crop)
    }

    suspend fun addPriceAlert(alert: PriceAlert): Long {
        return priceAlertDao.insert(alert)
    }

    suspend fun deletePriceAlert(alert: PriceAlert) {
        priceAlertDao.delete(alert)
    }
}
