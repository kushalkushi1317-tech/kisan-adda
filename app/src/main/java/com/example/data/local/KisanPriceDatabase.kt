package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.CropPrice
import com.example.data.model.FavoriteItem
import com.example.data.model.MyCrop
import com.example.data.model.PriceAlert

@Database(
    entities = [
        CropPrice::class,
        FavoriteItem::class,
        MyCrop::class,
        PriceAlert::class
    ],
    version = 1,
    exportSchema = false
)
abstract class KisanPriceDatabase : RoomDatabase() {
    abstract fun cropPriceDao(): CropPriceDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun myCropDao(): MyCropDao
    abstract fun priceAlertDao(): PriceAlertDao

    companion object {
        @Volatile
        private var INSTANCE: KisanPriceDatabase? = null

        fun getDatabase(context: Context): KisanPriceDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    KisanPriceDatabase::class.java,
                    "kisan_price_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
