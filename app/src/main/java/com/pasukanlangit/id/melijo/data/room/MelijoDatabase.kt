package com.pasukanlangit.id.melijo.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pasukanlangit.id.melijo.data.network.model.response.ProductItem
import com.pasukanlangit.id.melijo.data.network.model.response.PromoResultItem


@Database(entities = [ProductItem::class, PromoResultItem::class], version = 1, exportSchema = false)
abstract class MelijoDatabase : RoomDatabase(){
    abstract fun productDao(): ProductDao
    abstract fun promoDao(): PromoDao
}