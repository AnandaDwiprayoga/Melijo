package com.pasukanlangit.id.melijo.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pasukanlangit.id.melijo.data.network.model.response.ProductItem


@Database(entities = [ProductItem::class], version = 1, exportSchema = false)
abstract class MelijoDatabase : RoomDatabase(){
    abstract fun productDao(): ProductDao
}