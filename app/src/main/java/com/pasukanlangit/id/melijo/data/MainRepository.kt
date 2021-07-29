@file:Suppress("UNCHECKED_CAST")

package com.pasukanlangit.id.melijo.data

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.pasukanlangit.id.melijo.data.network.ApiService
import com.pasukanlangit.id.melijo.data.network.model.request.CategoryRequest
import com.pasukanlangit.id.melijo.data.network.model.request.LoginRequest
import com.pasukanlangit.id.melijo.data.network.model.request.RegisterRequest
import com.pasukanlangit.id.melijo.data.network.model.response.*
import com.pasukanlangit.id.melijo.data.room.ProductDao
import com.pasukanlangit.id.melijo.data.sharedpref.AuthSharedPref
import com.pasukanlangit.id.melijo.presentation.auth.UserType
import com.pasukanlangit.id.melijo.utils.MyNetwork
import com.pasukanlangit.id.melijo.utils.MyResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import java.net.SocketTimeoutException
import javax.inject.Inject

@Suppress("BlockingMethodInNonBlockingContext")
class MainRepository @Inject constructor(
    private val myNetwork: MyNetwork,
    private val apiService: ApiService,
    private val authSharedPref: AuthSharedPref,
    private val productDao: ProductDao
) {
    fun checkKeyIsValid(key: String) = flow {
        if (myNetwork.isOnline()){
            emit(MyResponse.Loading(null))

            try {
                val response = apiService.checkKeyIsValid(key)

                if(response.isSuccessful){
                    emit(MyResponse.Success(response.body()))
                }else{
                    val message = getErrorMessage(response.errorBody()?.string())
                    emit(MyResponse.Error(message, null))
                }
            }catch(timeOut: SocketTimeoutException){
                emit(MyResponse.Error("Terjadi Kesalahan", null))
            }
        }else{
            emit(MyResponse.Error("Check your internet connection", null))
        }
    }

    private fun getErrorMessage(errorMessage: String?): String {
        return errorMessage?.let { errorBody ->
            try {
                val responseError = Gson().fromJson(errorBody, MetaResponse::class.java)
                responseError.meta.message
            }catch (e: JsonSyntaxException){
                val responseError = Gson().fromJson(errorBody, MetaInputResponse::class.java)
                responseError.meta.message[0].errors[0]
            }
        } ?: "Terjadi kesalahan"
    }

    fun loginUser(loginRequest: LoginRequest) = flow {
        if (myNetwork.isOnline()){
            emit(MyResponse.Loading(null))

            try {
                val response = apiService.loginUser(loginRequest)

                if(response.isSuccessful){
                    emit(MyResponse.Success(response.body()))
                }else{
                    val message = getErrorMessage(response.errorBody()?.string())
                    emit(MyResponse.Error(message, null))
                }
            }catch(timeOut: SocketTimeoutException){
                emit(MyResponse.Error("Terjadi Kesalahan", null))
            }
        }else{
            emit(MyResponse.Error("Check your internet connection", null))
        }
    }

    fun loginProducer(loginRequest: LoginRequest) = flow {
        if (myNetwork.isOnline()){
            emit(MyResponse.Loading(null))

            try {
                val response = apiService.loginProducer(loginRequest)

                if(response.isSuccessful){
                    emit(MyResponse.Success(response.body()))
                }else{
                    val message = getErrorMessage(response.errorBody()?.string())
                    emit(MyResponse.Error(message, null))
                }
            }catch(timeOut: SocketTimeoutException){
                emit(MyResponse.Error("Terjadi Kesalahan", null))
            }
        }else{
            emit(MyResponse.Error("Check your internet connection", null))
        }
    }

    fun register(registerRequest: RegisterRequest, userType: UserType) = flow {
        if (myNetwork.isOnline()){
            emit(MyResponse.Loading(null))

            try {
                val response : Response<MetaResponse> = when (userType) {
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

                if(response.isSuccessful){
                    emit(MyResponse.Success(response.body()))
                }else{
                    val message = getErrorMessage(response.errorBody()?.string())
                    emit(MyResponse.Error(message, null))
                }
            }catch(timeOut: SocketTimeoutException){
                emit(MyResponse.Error("Terjadi Kesalahan", null))
            }
        }else{
            emit(MyResponse.Error("Check your internet connection", null))
        }
    }

    fun getListSellerByUser(accessToken: String) = flow {
        if (myNetwork.isOnline()){
            emit(MyResponse.Loading(null))

            try {
                val response = apiService.getListSellerByUser(accessToken)

                if(response.isSuccessful){
                    emit(MyResponse.Success(response.body()))
                }else{
                    val message = getErrorMessage(response.errorBody()?.string())
                    emit(MyResponse.Error(message, null))
                }
            }catch(timeOut: SocketTimeoutException){
                emit(MyResponse.Error("Terjadi Kesalahan", null))
            }
        }else{
            emit(MyResponse.Error("Check your internet connection", null))
        }
    }

    fun getDetailSeller(accessToken: String, idSeller: Int) : Flow<MyResponse<DetailSellerResponse>> =
        flow {
            if (myNetwork.isOnline()){
                emit(MyResponse.Loading(null))

                try {
                    val response = apiService.getSellerDetail(accessToken, idSeller)

                    if(response.isSuccessful){
                        emit(MyResponse.Success(response.body()))
                    }else{
                        val message = getErrorMessage(response.errorBody()?.string())
                        emit(MyResponse.Error(message, null))
                    }
                }catch(timeOut: SocketTimeoutException){
                    emit(MyResponse.Error("Terjadi Kesalahan", null))
                }
            }else{
                emit(MyResponse.Error("Check your internet connection", null))
            }
        } as Flow<MyResponse<DetailSellerResponse>>


    fun getAllProductSupplier(accessToken: String) : Flow<MyResponse<AllProductSupplierResponse>> =
        flow {
            if (myNetwork.isOnline()){
                emit(MyResponse.Loading(null))

                try {
                    val response = apiService.getAllProductSupplier(accessToken)

                    if(response.isSuccessful){
                        emit(MyResponse.Success(response.body()))
                    }else{
                        val message = getErrorMessage(response.errorBody()?.string())
                        emit(MyResponse.Error(message, null))
                    }
                }catch(timeOut: SocketTimeoutException){
                    emit(MyResponse.Error("Terjadi Kesalahan", null))
                }
            }else{
                emit(MyResponse.Error("Check your internet connection", null))
            }
        } as Flow<MyResponse<AllProductSupplierResponse>>

    fun getAllProductSupplierByCategory(accessToken: String, idCategory: Int) : Flow<MyResponse<AllProductSupplierResponse>> =
        flow {
            if (myNetwork.isOnline()){
                emit(MyResponse.Loading(null))

                try {
                    val response = apiService.getAllProductByCategory(accessToken,idCategory)

                    if(response.isSuccessful){
                        emit(MyResponse.Success(response.body()))
                    }else{
                        val message = getErrorMessage(response.errorBody()?.string())
                        emit(MyResponse.Error(message, null))
                    }
                }catch(timeOut: SocketTimeoutException){
                    emit(MyResponse.Error("Terjadi Kesalahan", null))
                }
            }else{
                emit(MyResponse.Error("Check your internet connection", null))
            }
        } as Flow<MyResponse<AllProductSupplierResponse>>

    fun getAllCategorySupplier(accessToken: String) : Flow<MyResponse<CategoryResponse>> =
        flow {
            if (myNetwork.isOnline()){
                emit(MyResponse.Loading(null))

                try {
                    val response = apiService.getAllCategorySupplier(accessToken)

                    if(response.isSuccessful){
                        emit(MyResponse.Success(response.body()))
                    }else{
                        val message = getErrorMessage(response.errorBody()?.string())
                        emit(MyResponse.Error(message, null))
                    }
                }catch(timeOut: SocketTimeoutException){
                    emit(MyResponse.Error("Terjadi Kesalahan", null))
                }
            }else{
                emit(MyResponse.Error("Check your internet connection", null))
            }
        } as Flow<MyResponse<CategoryResponse>>

    fun logout(accessToken: String, userType: UserType) = flow {
        if (myNetwork.isOnline()){
            emit(MyResponse.Loading(null))

            try {
                val response : Response<MetaResponse> = when (userType) {
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

                if(response.isSuccessful){
                    emit(MyResponse.Success(response.body()))
                }else{
                    val message = getErrorMessage(response.errorBody()?.string())
                    emit(MyResponse.Error(message, null))
                }
            }catch(timeOut: SocketTimeoutException){
                emit(MyResponse.Error("Terjadi Kesalahan", null))
            }
        }else{
            emit(MyResponse.Error("Check your internet connection", null))
        }
    }

    fun getProfileUser(accessToken: String) : Flow<MyResponse<UserProfileResponse>> =
        flow {
            if (myNetwork.isOnline()){
                emit(MyResponse.Loading(null))

                try {
                    val response = apiService.getProfileUser(accessToken)

                    if(response.isSuccessful){
                        emit(MyResponse.Success(response.body()))
                    }else{
                        val message = getErrorMessage(response.errorBody()?.string())
                        emit(MyResponse.Error(message, null))
                    }
                }catch(timeOut: SocketTimeoutException){
                    emit(MyResponse.Error("Terjadi Kesalahan", null))
                }
            }else{
                emit(MyResponse.Error("Check your internet connection", null))
            }
        } as Flow<MyResponse<UserProfileResponse>>

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
                emit(MyResponse.Error("Check yout internet connection", null))
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
            }
        } as Flow<MyResponse<CategoryResponse>>

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
        }
    }

    fun getProductSaved() = productDao.getAllProductFromCart()

    fun setAccessToken(token: String) = authSharedPref.setAccessToken(token)
    fun setImageUser(photo: String) = authSharedPref.setImageUser(photo)
    fun setNameUser(name: String) = authSharedPref.setNameUser(name)
    fun getAccessToken() : String? = authSharedPref.getAccessToken()
    fun removeAccessToken()  = authSharedPref.removeAccessToken()
    fun setAccountType(accountType: String) = authSharedPref.setAccountType(accountType)
    fun getAccountType() : String? = authSharedPref.getAccountType()
    fun getImageUser() : String? = authSharedPref.getImageUser()
    fun getNameUser() : String? = authSharedPref.getNameUser()
    fun setSession(value: Boolean) = authSharedPref.setIsSaveSession(value)
    fun getSession() : Boolean = authSharedPref.getIsSaveSession()
    suspend fun insertProduct(productItem: ProductItem) = productDao.insertProduct(productItem)
    suspend fun updateProduct(productItem: ProductItem) = productDao.updateProduct(productItem)
    suspend fun deleteProduct(productItem: ProductItem) = productDao.deleteProduct(productItem)
}