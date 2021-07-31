package com.pasukanlangit.id.melijo.presentation.mainprovider.home.product

import androidx.lifecycle.*
import com.pasukanlangit.id.melijo.data.MainRepository
import com.pasukanlangit.id.melijo.data.network.model.response.AllProductSupplierResponse
import com.pasukanlangit.id.melijo.utils.MyResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class ProductProviderViewModel @Inject constructor(
    private val mainRepository: MainRepository
    ): ViewModel() {
    private val _productsProvider = MutableLiveData<MyResponse<AllProductSupplierResponse>>()
    val productProvider : LiveData<MyResponse<AllProductSupplierResponse>> = _productsProvider

    private val accessToken = "Bearer 12|AJm1il583FAaI7PSEFqHLAz87kcOYCoLNlarJXvN"

    init {
        getAllProductsProvider()
    }

    private fun getAllProductsProvider() = viewModelScope.launch {
        accessToken.let { token ->
            mainRepository.getProductsProvider(token).collect {
                _productsProvider.value = it
            }
        }
    }

    fun createProductProvider(
        category: RequestBody,
        name: RequestBody,
        stock: RequestBody,
        price: RequestBody,
        promo: RequestBody,
        description: RequestBody?,
        picture: MultipartBody.Part?
    ) = mainRepository.createProductProvider(accessToken, category, name, stock, price, promo, description, picture).asLiveData()

    fun updateProductProvider(
        productId: Int,
        category: RequestBody,
        name: RequestBody,
        stock: RequestBody,
        price: RequestBody,
        promo: RequestBody,
        description: RequestBody?,
        picture: MultipartBody.Part?
    ) = mainRepository.updateProductProvider(accessToken, productId, category, name, stock, price, promo, description, picture).asLiveData()

    fun deleteProductProvider(productId: Int) = mainRepository.deleteProductProvider(accessToken, productId).asLiveData()
}