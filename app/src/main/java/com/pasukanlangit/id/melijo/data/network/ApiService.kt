package com.pasukanlangit.id.melijo.data.network

import com.pasukanlangit.id.melijo.data.network.model.request.CategoryRequest
import com.pasukanlangit.id.melijo.data.network.model.request.LoginRequest
import com.pasukanlangit.id.melijo.data.network.model.request.OrderRequest
import com.pasukanlangit.id.melijo.data.network.model.request.RegisterRequest
import com.pasukanlangit.id.melijo.data.network.model.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    @GET("transaction")
    suspend fun getTransactionForBuyer(@Header("Authorization") token: String) : Response<OrderBuyerResponse>

    @GET("promo")
    suspend fun getPromoForUser(@Header("Authorization") token: String, @Query("level") level: String ?= null) : Response<AllPromoResponse>

    @POST("transaction")
    suspend fun createTransactionBuyer(@Header("Authorization") token: String, @Body orderRequest: OrderRequest) : Response<OrderResponse>

    @PATCH("transaction/cancel/{trxId}")
    suspend fun cancelTrxBuyer(@Header("Authorization") token: String, @Path("trxId") trxId: Int) : Response<MetaResponse>


    @Multipart
    @POST("user/profile")
    suspend fun updateProfileUser(
        @Header("Authorization") token: String,
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("address") address: RequestBody,
        @Part("phoneNumber") phoneNumber: RequestBody,
        @Part image : MultipartBody.Part?
    ) : Response<MetaResponse>

    @POST("producer/category")
    suspend fun createCategoryProvider(@Header("Authorization") token: String, @Body mCategoryRequest: CategoryRequest): Response<MetaResponse>

    @GET("producer/category")
    suspend fun getCategoryProvider(@Header("Authorization") token: String): Response<CategoryResponse>

    @POST("producer/category/{category_id}")
    suspend fun updateCategoryProvider(@Header("Authorization") token: String, @Path("category_id") category_id: Int, @Body mCategoryRequest: CategoryRequest): Response<MetaResponse>

    @DELETE("producer/category/{category_id}")
    suspend fun deleteCategoryProvider(@Header("Authorization") token: String, @Path("category_id") category_Id: Int): Response<MetaResponse>

    @Multipart
    @POST("producer/product")
    suspend fun createProductsProvider(
        @Header("Authorization") token: String,
        @Part("category_id") category_Id: RequestBody,
        @Part("name") name: RequestBody,
        @Part("stock") stock: RequestBody,
        @Part("price") price: RequestBody,
        @Part("promo") discount: RequestBody,
        @Part("description") description: RequestBody?,
        @Part picture: MultipartBody.Part?
    ): Response<MetaResponse>

    @GET("producer/product")
    suspend fun getProductsProvider(@Header("Authorization") token: String): Response<AllProductSupplierResponse>

    @GET("producer/profile")
    suspend fun getProfileProducer(@Header("Authorization") token: String): Response<ProfilProducerResponse>

    @Multipart
    @POST("producer/product/{product_id}")
    suspend fun updateProductProvider(
        @Header("Authorization")token: String,
        @Path("product_id") product_id: Int,
        @Part("category_id") category_Id: RequestBody,
        @Part("name") name: RequestBody,
        @Part("stock") stock: RequestBody,
        @Part("price") price: RequestBody,
        @Part("promo") discount: RequestBody,
        @Part("description") description: RequestBody?,
        @Part photo: MultipartBody.Part?
    ): Response<MetaResponse>

    @DELETE("producer/product/{product_id}")
    suspend fun deleteProductProvider(@Header("Authorization") token: String, @Path("product_id") product_id: Int): Response<MetaResponse>
}