package com.pasukanlangit.id.melijo.data.network

import com.pasukanlangit.id.melijo.data.network.model.request.CategoryRequest
import com.pasukanlangit.id.melijo.data.network.model.request.CreateProductRequest
import com.pasukanlangit.id.melijo.data.network.model.request.LoginRequest
import com.pasukanlangit.id.melijo.data.network.model.request.RegisterRequest
import com.pasukanlangit.id.melijo.data.network.model.response.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("development/key")
    suspend fun checkKeyIsValid(@Query("key") key: String) : Response<MetaResponse>

    @POST("auth/user")
    suspend fun loginUser(@Body loginRequest: LoginRequest) : Response<LoginUserResponse>

    @POST("auth/producer")
    suspend fun loginProducer(@Body loginRequest: LoginRequest) : Response<LoginProducerResponse>

    @POST("auth/register/user")
    suspend fun registerUser(@Body registerRequest: RegisterRequest) : Response<MetaResponse>

    @POST("auth/register/seller")
    suspend fun registerSeller(@Body registerRequest: RegisterRequest) : Response<MetaResponse>

    @POST("auth/register/supplier")
    suspend fun registerSupplier(@Body registerRequest: RegisterRequest) : Response<MetaResponse>

    @POST("logout/producer")
    suspend fun logoutProducer(@Header("Authorization") token: String) : Response<MetaResponse>

    @POST("logout/user")
    suspend fun logoutUser(@Header("Authorization") token: String) : Response<MetaResponse>

    @GET("user/sellers")
    suspend fun getListSellerByUser(@Header("Authorization") token: String) : Response<SellerListResponse>

    @GET("user/seller/detail/{idSeller}")
    suspend fun getSellerDetail(@Header("Authorization") token: String, @Path("idSeller") idSeller: Int) : Response<DetailSellerResponse>

    @GET("product")
    suspend fun getAllProductSupplier(@Header("Authorization") token: String) : Response<AllProductSupplierResponse>

    @GET("product")
    suspend fun getAllCategorySupplier(@Header("Authorization") token: String) : Response<CategoryResponse>

    @GET("product")
    suspend fun getAllProductByCategory(@Header("Authorization") token: String, @Query("category")categoryId: Int) : Response<AllProductSupplierResponse>

    @GET("user/profile")
    suspend fun getProfileUser(@Header("Authorization") token: String) : Response<UserProfileResponse>

    @POST("producer/category")
    suspend fun createCategoryProvider(@Header("Authorization") token: String, @Body mCategoryRequest: CategoryRequest): Response<MetaResponse>

    @GET("producer/category")
    suspend fun getCategoryProvider(@Header("Authorization") token: String): Response<CategoryResponse>

    @POST("producer/category/{category_id}")
    suspend fun updateCategoryProvider(@Header("Authorization") token: String, @Path("category_id") category_id: Int, @Body mCategoryRequest: CategoryRequest): Response<MetaResponse>

    @DELETE("producer/category/{category_id}")
    suspend fun deleteCategoryProvider(@Header("Authorization") token: String, @Path("category_id") category_Id: Int): Response<MetaResponse>

    @POST("producer/product")
    suspend fun createProductsProvider(@Body createProductRequest: CreateProductRequest): Response<MetaResponse>

    @GET("producer/product")
    suspend fun getProductsProvider(@Header("Authorization") token: String): Response<AllProductSupplierResponse>
}