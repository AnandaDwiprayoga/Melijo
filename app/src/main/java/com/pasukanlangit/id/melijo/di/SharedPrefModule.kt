package com.pasukanlangit.id.melijo.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedPrefModule {

    @Provides
    @Singleton
    fun provideSharedPref(@ApplicationContext context: Context) : SharedPreferences =
        context.getSharedPreferences("MlinjoSharedPref", Context.MODE_PRIVATE)
}