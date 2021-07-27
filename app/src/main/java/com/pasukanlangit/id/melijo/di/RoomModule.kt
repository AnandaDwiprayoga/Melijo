package com.pasukanlangit.id.melijo.di

import android.content.Context
import androidx.room.Room
import com.pasukanlangit.id.melijo.data.room.MelijoDatabase
import com.pasukanlangit.id.melijo.data.room.ProductDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MelijoDatabase =
        Room.databaseBuilder(context, MelijoDatabase::class.java, "melijo_database").build()

    @Provides
    fun provideProductDao(db : MelijoDatabase) : ProductDao = db.productDao()
}