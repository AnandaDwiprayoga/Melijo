package com.pasukanlangit.id.melijo.presentation.main.home.seller.detial

import androidx.lifecycle.*
import com.pasukanlangit.id.melijo.data.MainRepository
import com.pasukanlangit.id.melijo.data.network.model.response.DetailSellerResponse
import com.pasukanlangit.id.melijo.data.network.model.response.ProductItem
import com.pasukanlangit.id.melijo.utils.MyResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailSellerViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel(){


    private val _detailSeller = MutableLiveData<MyResponse<DetailSellerResponse>>()
    val detailSeller : LiveData<MyResponse<DetailSellerResponse>> = _detailSeller

    private val _productCartSaved = MutableLiveData<List<ProductItem>>()
    val productCartSaved : LiveData<List<ProductItem>> = _productCartSaved

    private val _productCartSync = MutableLiveData<List<ProductItem>?>()
    val productCartSync : LiveData<List<ProductItem>?> = _productCartSync


    fun collectProductSaved(ownerId: Int) = viewModelScope.launch {
        mainRepository.getProductSaved(ownerId).collect {
            _productCartSaved.value = it
            syncServerWithDb(detailSeller.value?.data?.result?.product)
        }
    }
//    val productCartSaved = mainRepository.getProductSaved().asLiveData()

    fun getAccessToken() : String? = mainRepository.getAccessToken()

    fun getDetailSeller(accessToken: String, idSeller: Int) = viewModelScope.launch {
        mainRepository.getDetailSeller(accessToken, idSeller).collect {
             syncServerWithDb(it.data?.result?.product)
            _detailSeller.postValue(it)
        }
    }

    private fun syncServerWithDb(products: List<ProductItem>?){
        val productSaved = productCartSaved.value
        val productSync = mutableListOf<ProductItem>()
        products?.forEach { product ->
            val matchesProduct = productSaved?.singleOrNull { productSaved ->
                productSaved.id == product.id }
            if(matchesProduct != null) productSync.add(product.copy(qty = matchesProduct.qty))
            else productSync.add(product.copy(qty = 0))
        }
        _productCartSync.postValue(productSync)
    }

    fun saveProductToCart(productItem: ProductItem) = viewModelScope.launch {
        mainRepository.insertProduct(productItem)
    }

    fun updateProductCart(productItem: ProductItem) = viewModelScope.launch {
        mainRepository.updateProduct(productItem)
    }

    fun deleteProductCart(productItem: ProductItem) = viewModelScope.launch {
        mainRepository.deleteProduct(productItem)
    }
}