@file:Suppress("UNCHECKED_CAST")

package com.pasukanlangit.id.melijo.data

import androidx.room.withTransaction
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.pasukanlangit.id.melijo.data.network.ApiService
import com.pasukanlangit.id.melijo.data.network.model.request.CategoryRequest
import com.pasukanlangit.id.melijo.data.network.model.request.LoginRequest
import com.pasukanlangit.id.melijo.data.network.model.request.OrderRequest
import com.pasukanlangit.id.melijo.data.network.model.request.RegisterRequest
import com.pasukanlangit.id.melijo.data.network.model.response.*
import com.pasukanlangit.id.melijo.data.room.MelijoDatabase
import com.pasukanlangit.id.melijo.data.room.ProductDao
import com.pasukanlangit.id.melijo.data.room.PromoDao
import com.pasukanlangit.id.melijo.data.sharedpref.AuthSharedPref
import com.pasukanlangit.id.melijo.presentation.auth.UserType
import com.pasukanlangit.id.melijo.utils.MyNetwork
import com.pasukanlangit.id.melijo.utils.MyResponse
import com.pasukanlangit.id.melijo.utils.SingleEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.net.SocketTimeoutException
import javax.inject.Inject

@Suppress("BlockingMethodInNonBlockingContext")
class MainRepository @Inject constructor(
    private val myNetwork: MyNetwork,
    private val apiService: ApiService,
    private val authSharedPref: AuthSharedPref,
    private val database: MelijoDatabase,
    private val productDao: ProductDao,
    private val promoDao: PromoDao
) {
    fun checkKeyIsValid(key: String) = flow {
        if (myNetwork.isOnline()) {
            emit(MyResponse.Loading(null))

            try {
                val response = apiService.checkKeyIsValid(key)

                if (response.isSuccessful) {
                    emit(MyResponse.Success(response.body()))
                } else {
                    val message = getErrorMessage(response.errorBody()?.string())
                    emit(MyResponse.Error(message, null))
                }
            } catch (timeOut: SocketTimeoutException) {
                emit(MyResponse.Error("Terjadi Kesalahan", null))
            }
        } else {
            emit(MyResponse.Error("Check your internet connection", null))
        }
    }

    private fun getErrorMessage(errorMessage: String?): String {
        return errorMessage?.let { errorBody ->
            try {
                val responseError = Gson().fromJson(errorBody, MetaResponse::class.java)
                responseError.meta.message
            } catch (e: JsonSyntaxException) {
                val responseError = Gson().fromJson(errorBody, MetaInputResponse::class.java)
                responseError.meta.message[0].errors[0]
            }
        } ?: "Terjadi kesalahan"
    }

    fun loginUser(loginRequest: LoginRequest) = flow {
        if (myNetwork.isOnline()) {
            emit(MyResponse.Loading(null))

            try {
                val response = apiService.loginUser(loginRequest)

                if (response.isSuccessful) {
                    emit(MyResponse.Success(response.body()))
                } else {
                    val message = getErrorMessage(response.errorBody()?.string())
                    emit(MyResponse.Error(message, null))
                }
            } catch (timeOut: SocketTimeoutException) {
                emit(MyResponse.Error("Terjadi Kesalahan", null))
            }
        } else {
            emit(MyResponse.Error("Check your internet connection", null))
        }
    }

    fun loginProducer(loginRequest: LoginRequest) = flow {
        if (myNetwork.isOnline()) {
            emit(MyResponse.Loading(null))

            try {
                val response = apiService.loginProducer(loginRequest)

                if (response.isSuccessful) {
                    emit(MyResponse.Success(response.body()))
                } else {
                    val message = getErrorMessage(response.errorBody()?.string())
                    emit(MyResponse.Error(message, null))
                }
            } catch (timeOut: SocketTimeoutException) {
                emit(MyResponse.Error("Terjadi Kesalahan", null))
            }
        } else {
            emit(MyResponse.Error("Check your internet connection", null))
        }
    }

    fun register(registerRequest: RegisterRequest, userType: UserType) = flow {
        if (myNetwork.isOnline()) {
            emit(MyResponse.Loading(null))

            try {
                val response: Response<MetaResponse> = when (userType) {
                    UserType.TYPE_BUYER -> {
                        apiService.registerUser(registerRequest)
                    }
                    UserType.TYPE_SUPPLIER -> {
                        apiService.registerSupplier(registerRequest)
                    }
                    UserType.TYPE_SELLER -> {
                        apiService.registerSeller(registerRequest)
                    }
                }

                if (response.isSuccessful) {
                    emit(MyResponse.Success(response.body()))
                } else {
                    val message = getErrorMessage(response.errorBody()?.string())
                    emit(MyResponse.Error(message, null))
                }
            } catch (timeOut: SocketTimeoutException) {
                emit(MyResponse.Error("Terjadi Kesalahan", null))
            }
        } else {
            emit(MyResponse.Error("Check your internet connection", null))
        }
    }

    fun getListSellerByUser(accessToken: String) = flow {
        if (myNetwork.isOnline()) {
            emit(MyResponse.Loading(null))

            try {
                val response = apiService.getListSellerByUser(accessToken)

                if (response.isSuccessful) {
                    emit(MyResponse.Success(response.body()))
                } else {
                    val message = getErrorMessage(response.errorBody()?.string())
                    emit(MyResponse.Error(message, null))
                }
            } catch (timeOut: SocketTimeoutException) {
                emit(MyResponse.Error("Terjadi Kesalahan", null))
            }
        } else {
            emit(MyResponse.Error("Check your internet connection", null))
        }
    }

    fun getDetailSeller(
        accessToken: String,
        idSeller: Int
    ): Flow<MyResponse<DetailSellerResponse>> =
        flow {
            if (myNetwork.isOnline()) {
                emit(MyResponse.Loading(null))

                try {
                    val response = apiService.getSellerDetail(accessToken, idSeller)

                    if (response.isSuccessful) {
                        emit(MyResponse.Success(response.body()))
                    } else {
                        val message = getErrorMessage(response.errorBody()?.string())
                        emit(MyResponse.Error(message, null))
                    }
                } catch (timeOut: SocketTimeoutException) {
                    emit(MyResponse.Error("Terjadi Kesalahan", null))
                }
            } else {
                emit(MyResponse.Error("Check your internet connection", null))
            }
        } as Flow<MyResponse<DetailSellerResponse>>


    fun getAllProductSupplier(accessToken: String): Flow<MyResponse<AllProductSupplierResponse>> =
        flow {
            if (myNetwork.isOnline()) {
                emit(MyResponse.Loading(null))

                try {
                    val response = apiService.getAllProductSupplier(accessToken)

                    if (response.isSuccessful) {
                        emit(MyResponse.Success(response.body()))
                    } else {
                        val message = getErrorMessage(response.errorBody()?.string())
                        emit(MyResponse.Error(message, null))
                    }
                } catch (timeOut: SocketTimeoutException) {
                    emit(MyResponse.Error("Terjadi Kesalahan", null))
                }
            } else {
                emit(MyResponse.Error("Check your internet connection", null))
            }
        } as Flow<MyResponse<AllProductSupplierResponse>>

    fun getAllProductSupplierByCategory(
        accessToken: String,
        idCategory: Int
    ): Flow<MyResponse<AllProductSupplierResponse>> =
        flow {
            if (myNetwork.isOnline()) {
                emit(MyResponse.Loading(null))

                try {
                    val response = apiService.getAllProductByCategory(accessToken, idCategory)

                    if (response.isSuccessful) {
                        emit(MyResponse.Success(response.body()))
                    } else {
                        val message = getErrorMessage(response.errorBody()?.string())
                        emit(MyResponse.Error(message, null))
                    }
                } catch (timeOut: SocketTimeoutException) {
                    emit(MyResponse.Error("Terjadi Kesalahan", null))
                }
            } else {
                emit(MyResponse.Error("Check your internet connection", null))
            }
        } as Flow<MyResponse<AllProductSupplierResponse>>

    fun getAllCategorySupplier(accessToken: String): Flow<MyResponse<CategoryResponse>> =
        flow {
            if (myNetwork.isOnline()) {
                emit(MyResponse.Loading(null))

                try {
                    val response = apiService.getAllCategorySupplier(accessToken)

                    if (response.isSuccessful) {
                        emit(MyResponse.Success(response.body()))
                    } else {
                        val message = getErrorMessage(response.errorBody()?.string())
                        emit(MyResponse.Error(message, null))
                    }
                } catch (timeOut: SocketTimeoutException) {
                    emit(MyResponse.Error("Terjadi Kesalahan", null))
                }
            } else {
                emit(MyResponse.Error("Check your internet connection", null))
            }
        } as Flow<MyResponse<CategoryResponse>>

    fun logout(accessToken: String, userType: UserType) = flow {
        if (myNetwork.isOnline()) {
            emit(MyResponse.Loading(null))

            try {
                val response: Response<MetaResponse> = when (userType) {
                    UserType.TYPE_BUYER -> {
                        apiService.logoutUser(accessToken)
                    }
                    UserType.TYPE_SUPPLIER -> {
                        apiService.logoutProducer(accessToken)
                    }
                    UserType.TYPE_SELLER -> {
                        apiService.logoutProducer(accessToken)
                    }
                }

                if (response.isSuccessful) {
                    emit(MyResponse.Success(response.body()))
                } else {
                    val message = getErrorMessage(response.errorBody()?.string())
                    emit(MyResponse.Error(message, null))
                }
            } catch (timeOut: SocketTimeoutException) {
                emit(MyResponse.Error("Terjadi Kesalahan", null))
            }
        } else {
            emit(MyResponse.Error("Check your internet connection", null))
        }
    }

    fun getProfileUser(accessToken: String): Flow<MyResponse<UserProfileResponse>> =
        flow {
            if (myNetwork.isOnline()) {
                emit(MyResponse.Loading(null))

                try {
                    val response = apiService.getProfileUser(accessToken)

                    if (response.isSuccessful) {
                        emit(MyResponse.Success(response.body()))
                    } else {
                        val message = getErrorMessage(response.errorBody()?.string())
                        emit(MyResponse.Error(message, null))
                    }
                } catch (timeOut: SocketTimeoutException) {
                    emit(MyResponse.Error("Terjadi Kesalahan", null))
                }
            } else {
                emit(MyResponse.Error("Check your internet connection", null))
            }
        } as Flow<MyResponse<UserProfileResponse>>

    fun getAllPromoForUser(
        accessToken: String,
        level: String? = null
    ): Flow<MyResponse<AllPromoResponse>> =
        flow {
            if (myNetwork.isOnline()) {
                emit(MyResponse.Loading(null))

                try {
                    val response = apiService.getPromoForUser(accessToken, level)

                    if (response.isSuccessful) {
                        emit(MyResponse.Success(response.body()))
                    } else {
                        val message = getErrorMessage(response.errorBody()?.string())
                        emit(MyResponse.Error(message, null))
                    }
                } catch (timeOut: SocketTimeoutException) {
                    emit(MyResponse.Error("Terjadi Kesalahan", null))
                }
            } else {
                emit(MyResponse.Error("Check your internet connection", null))
            }
        } as Flow<MyResponse<AllPromoResponse>>

    fun getProductsProvider(accessToken: String): Flow<MyResponse<AllProductSupplierResponse>> =
        flow {
            if (myNetwork.isOnline()) {
                emit(MyResponse.Loading(null))
                try {
                    val response = apiService.getProductsProvider(accessToken)

                    if (response.isSuccessful) {
                        emit(MyResponse.Success(response.body()))
                    } else {
                        val message = getErrorMessage(response.errorBody()?.string())
                        emit(MyResponse.Error(message, null))
                    }
                } catch (timeOut: SocketTimeoutException) {
                    emit(MyResponse.Error("Terjadi Kesalahan", null))
                }
            } else {
                emit(MyResponse.Error("Check your internet connection", null))
            }
        } as Flow<MyResponse<AllProductSupplierResponse>>

    fun getCategoryProvider(accessToken: String): Flow<MyResponse<CategoryResponse>> =
        flow {
            if (myNetwork.isOnline()) {
                emit(MyResponse.Loading(null))
                try {
                    val response = apiService.getCategoryProvider(accessToken)

                    if (response.isSuccessful) {
                        emit(MyResponse.Success(response.body()))
                    } else {
                        val message = getErrorMessage(response.errorBody()?.string())
                        emit(MyResponse.Error(message, null))
                    }
                } catch (timeOut: SocketTimeoutException) {
                    emit(MyResponse.Error("Terjadi Kesalahan", null))
                }
            } else {
                emit(MyResponse.Error("Check yout internet connection", null))
            }
        } as Flow<MyResponse<CategoryResponse>>

    fun createTransactionForBuyer(
        accessToken: String,
        orderRequest: OrderRequest
    ): Flow<MyResponse<SingleEvent<OrderResponse>>> =
        flow {
            if (myNetwork.isOnline()) {
                emit(MyResponse.Loading(null))

                try {
                    val response = apiService.createTransactionBuyer(accessToken, orderRequest)

                    if (response.isSuccessful) {
                        response.body()?.let { body ->
                            emit(MyResponse.Success(SingleEvent(body)))
                        }
                    } else {
                        val message = getErrorMessage(response.errorBody()?.string())
                        emit(MyResponse.Error(message, null))
                    }
                } catch (timeOut: SocketTimeoutException) {
                    emit(MyResponse.Error("Terjadi Kesalahan", null))
                }
            } else {
                emit(MyResponse.Error("Check your internet connection", null))
            }
        } as Flow<MyResponse<SingleEvent<OrderResponse>>>

    fun cancelTransaction(accessToken: String, trxId: Int): Flow<MyResponse<MetaResponse>> =
        flow {
            if (myNetwork.isOnline()) {
                emit(MyResponse.Loading(null))

                try {
                    val response = apiService.cancelTrxBuyer(accessToken, trxId)

                    if (response.isSuccessful) {
                        emit(MyResponse.Success(response.body()))
                    } else {
                        val message = getErrorMessage(response.errorBody()?.string())
                        emit(MyResponse.Error(message, null))
                    }
                } catch (timeOut: SocketTimeoutException) {
                    emit(MyResponse.Error("Terjadi Kesalahan", null))
                }
            } else {
                emit(MyResponse.Error("Check your internet connection", null))
            }
        } as Flow<MyResponse<MetaResponse>>

    fun createCategory(accessToken: String, mCategoryRequest: CategoryRequest) = flow {
        if (myNetwork.isOnline()) {
            emit(MyResponse.Loading(null))
            try {
                val response = apiService.createCategoryProvider(accessToken, mCategoryRequest)

                if (response.isSuccessful) {
                    emit(MyResponse.Success(response.body()))
                } else {
                    val message = getErrorMessage(response.errorBody()?.string())
                    emit(MyResponse.Error(message, null))
                }
            } catch (timeOut: SocketTimeoutException) {
                emit(MyResponse.Error("Terjadi Kesalahan", null))
            }
        } else {
            emit(MyResponse.Error("Check yout internet connection", null))
        }
    }

    fun updateCategory(accessToken: String, categoryId: Int, mCategoryRequest: CategoryRequest) = flow {
        if (myNetwork.isOnline()) {
            emit(MyResponse.Loading(null))
            try {
                val response = apiService.updateCategoryProvider(accessToken, categoryId, mCategoryRequest)

                if (response.isSuccessful) {
                    emit(MyResponse.Success(response.body()))
                } else {
                    val message = getErrorMessage(response.errorBody()?.string())
                    emit(MyResponse.Error(message, null))
                }
            } catch (timeOut: SocketTimeoutException) {
                emit(MyResponse.Error("Terjadi Kesalahan", null))
            }
        } else {
            emit(MyResponse.Error("Check yout internet connection", null))
        }
    }

    fun deleteCategory(accessToken: String, categoryId: Int) = flow {
        if (myNetwork.isOnline()) {
            emit(MyResponse.Loading(null))
            try {
                val response = apiService.deleteCategoryProvider(accessToken, categoryId)

                if (response.isSuccessful) {
                    emit(MyResponse.Success(response.body()))
                } else {
                    val message = getErrorMessage(response.errorBody()?.string())
                    emit(MyResponse.Error(message, null))
                }
            } catch (timeOut: SocketTimeoutException) {
                emit(MyResponse.Error("Terjadi Kesalahan", null))
            }
        } else {
            emit(MyResponse.Error("Check yout internet connection", null))
        }
    }

    fun getTransactionForBuyer(accessToken: String): Flow<MyResponse<OrderBuyerResponse>> =
        flow {
            if (myNetwork.isOnline()) {
                emit(MyResponse.Loading(null))
                try {
                    val response = apiService.getTransactionForBuyer(accessToken)

                    if (response.isSuccessful) {
                        emit(MyResponse.Success(response.body()))
                    } else {
                        val message = getErrorMessage(response.errorBody()?.string())
                        emit(MyResponse.Error(message, null))
                    }
                } catch (timeOut: SocketTimeoutException) {
                    emit(MyResponse.Error("Terjadi Kesalahan", null))
                }
            }
        } as Flow<MyResponse<OrderBuyerResponse>>

    fun getProfileProducer(accessToken: String): Flow<MyResponse<ProfilProducerResponse>> =
        flow {
            if (myNetwork.isOnline()) {
                emit(MyResponse.Loading(null))
                try {
                    val response = apiService.getProfileProducer(accessToken)

                    if (response.isSuccessful) {
                        emit(MyResponse.Success(response.body()))
                    } else {
                        val message = getErrorMessage(response.errorBody()?.string())
                        emit(MyResponse.Error(message, null))
                    }
                } catch (timeOut: SocketTimeoutException) {
                    emit(MyResponse.Error("Terjadi Kesalahan", null))
                }
            }
        } as Flow<MyResponse<ProfilProducerResponse>>


    fun createProductProvider(
        accessToken: String,
        categoryId: RequestBody,
        name: RequestBody,
        stock: RequestBody,
        price: RequestBody,
        promo: RequestBody,
        description: RequestBody?,
        picture: MultipartBody.Part?
    ) = flow {
        if (myNetwork.isOnline()) {
            emit(MyResponse.Loading(null))
            try {
                val response = apiService.createProductsProvider(
                    accessToken,
                    categoryId,
                    name,
                    stock,
                    price,
                    promo,
                    description,
                    picture
                )

                if (response.isSuccessful) {
                    emit(MyResponse.Success(response.body()))
                } else {
                    val message = getErrorMessage(response.errorBody()?.string())
                    emit(MyResponse.Error(message, null))
                }
            } catch (timeOut: SocketTimeoutException) {
                emit(MyResponse.Error("Terjadi Kesalahan", null))
            }
        } else {
            emit(MyResponse.Error("Check your internet connection", null))
        }
    }


    fun updateProductProvider(
        accessToken: String,
        productId: Int,
        categoryId: RequestBody,
        name: RequestBody,
        stock: RequestBody,
        price: RequestBody,
        promo: RequestBody,
        description: RequestBody?,
        picture: MultipartBody.Part?
    ) = flow {
        if (myNetwork.isOnline()) {
            emit(MyResponse.Loading(null))
            try {
                val response = apiService.updateProductProvider(
                    accessToken,
                    productId,
                    categoryId,
                    name,
                    stock,
                    price,
                    promo,
                    description,
                    picture
                )

                if (response.isSuccessful) {
                    emit(MyResponse.Success(response.body()))
                } else {
                    val message = getErrorMessage(response.errorBody()?.string())
                    emit(MyResponse.Error(message, null))
                }
            } catch (timeOut: SocketTimeoutException) {
                emit(MyResponse.Error("Terjadi Kesalahan", null))
            }
        } else {
            emit(MyResponse.Error("Check your internet connection", null))
        }
    }

    fun deleteProductProvider(accessToken: String, productId: Int) = flow {
        if (myNetwork.isOnline()) {
            try {
                val response = apiService.deleteProductProvider(accessToken, productId)

                if (response.isSuccessful) {
                    emit(MyResponse.Success(response.body()))
                } else {
                    val message = getErrorMessage(response.errorBody()?.string())
                    emit(MyResponse.Error(message, null))
                }
            } catch (timeOut: SocketTimeoutException) {
                emit(MyResponse.Error("Terjadi Kesalahan", null))
            }
        } else {
            emit(MyResponse.Error("Check yout internet connection", null))
        }
    }

    fun updateProfileUser(
        token: String,
        name: RequestBody,
        email: RequestBody,
        address: RequestBody,
        phoneNumber: RequestBody,
        image: MultipartBody.Part?
    ): Flow<MyResponse<MetaResponse>> =
        flow {
            if (myNetwork.isOnline()) {
                emit(MyResponse.Loading(null))

                try {
                    val response = apiService.updateProfileUser(
                        token,
                        name,
                        email,
                        address,
                        phoneNumber,
                        image
                    )

                    if (response.isSuccessful) {
                        emit(MyResponse.Success(response.body()))
                    } else {
                        val message = getErrorMessage(response.errorBody()?.string())
                        emit(MyResponse.Error(message, null))
                    }
                } catch (timeOut: SocketTimeoutException) {
                    emit(MyResponse.Error("Terjadi Kesalahan", null))
                }
            } else {
                emit(MyResponse.Error("Check your internet connection", null))
            }
        } as Flow<MyResponse<MetaResponse>>

    fun getProductSaved(ownerId: Int) = productDao.getAllProductFromCart(ownerId)

    fun setAccessToken(token: String) = authSharedPref.setAccessToken(token)
    fun setImageUser(photo: String) = authSharedPref.setImageUser(photo)
    fun setNameUser(name: String) = authSharedPref.setNameUser(name)
    fun getAccessToken(): String? = authSharedPref.getAccessToken()
    fun removeAccessToken() = authSharedPref.removeAccessToken()
    fun setAccountType(accountType: String) = authSharedPref.setAccountType(accountType)
    fun getAccountType(): String? = authSharedPref.getAccountType()
    fun getImageUser(): String? = authSharedPref.getImageUser()
    fun getNameUser(): String? = authSharedPref.getNameUser()
    fun setSession(value: Boolean) = authSharedPref.setIsSaveSession(value)
    fun getSession(): Boolean = authSharedPref.getIsSaveSession()
    suspend fun insertProduct(productItem: ProductItem) = productDao.insertProduct(productItem)
    suspend fun updateProduct(productItem: ProductItem) = productDao.updateProduct(productItem)
    suspend fun deleteProduct(productItem: ProductItem) = productDao.deleteProduct(productItem)
    suspend fun deletePromo() = promoDao.deleteAllPromo()
    suspend fun insertPromo(promoResultItem: PromoResultItem) {
        database.withTransaction {
            promoDao.deleteAllPromo()
            promoDao.insertPromo(promoResultItem)
        }
    }

    fun getPromoSelected() = promoDao.getMainPromo()

}