package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.CropPrice
import com.example.data.model.FavoriteItem
import com.example.data.model.MyCrop
import com.example.data.model.PriceAlert
import kotlinx.coroutines.flow.Flow

@Dao
interface CropPriceDao {
    @Query("SELECT * FROM crop_prices ORDER BY avgPrice DESC")
    fun getAllCropPrices(): Flow<List<CropPrice>>

    @Query("SELECT * FROM crop_prices WHERE id = :id")
    suspend fun getCropPriceById(id: Long): CropPrice?

    @Query("SELECT * FROM crop_prices WHERE LOWER(cropName) = LOWER(:name)")
    fun getPricesForCrop(name: String): Flow<List<CropPrice>>

    @Query("SELECT COUNT(*) FROM crop_prices")
    suspend fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(prices: List<CropPrice>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(price: CropPrice): Long

    @Update
    suspend fun update(price: CropPrice)

    @Delete
    suspend fun delete(price: CropPrice)

    @Query("DELETE FROM crop_prices")
    suspend fun deleteAll()
}

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorites")
    fun getAllFavorites(): Flow<List<FavoriteItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: FavoriteItem)

    @Query("DELETE FROM favorites WHERE cropId = :cropId")
    suspend fun deleteById(cropId: Long)

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE cropId = :cropId)")
    fun isFavorite(cropId: Long): Flow<Boolean>
}

@Dao
interface MyCropDao {
    @Query("SELECT * FROM my_crops")
    fun getAllMyCrops(): Flow<List<MyCrop>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(myCrop: MyCrop): Long

    @Delete
    suspend fun delete(myCrop: MyCrop)
}

@Dao
interface PriceAlertDao {
    @Query("SELECT * FROM price_alerts ORDER BY createdAt DESC")
    fun getAllAlerts(): Flow<List<PriceAlert>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(alert: PriceAlert): Long

    @Delete
    suspend fun delete(alert: PriceAlert)
}
