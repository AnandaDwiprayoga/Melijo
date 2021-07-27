package com.pasukanlangit.id.melijo.di

import com.pasukanlangit.id.melijo.BuildConfig
import com.pasukanlangit.id.melijo.data.network.ApiService
import com.pasukanlangit.id.melijo.utils.MyNetwork
import com.pasukanlangit.id.melijo.utils.MyNetworkImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideOkHttpClient() =  if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    } else OkHttpClient.Builder()
        .retryOnConnectionFailure(true)
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)


    @Provides
    @Singleton
    fun provideMyNetwork() : MyNetwork = MyNetworkImpl()

}