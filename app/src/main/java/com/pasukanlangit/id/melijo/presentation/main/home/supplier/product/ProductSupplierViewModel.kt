package com.pasukanlangit.id.melijo.presentation.main.home.supplier.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pasukanlangit.id.melijo.data.MainRepository
import com.pasukanlangit.id.melijo.data.network.model.response.AllProductSupplierResponse
import com.pasukanlangit.id.melijo.data.network.model.response.CategoryResponse
import com.pasukanlangit.id.melijo.data.network.model.response.ProductItem
import com.pasukanlangit.id.melijo.utils.MyResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductSupplierViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {
    companion object {
        const val OWNER_ID_SUPPLIER = -1111
    }

    private val _products = MutableLiveData<MyResponse<AllProductSupplierResponse>>()
    val products : LiveData<MyResponse<AllProductSupplierResponse>> = _products

    private val _category = MutableLiveData<MyResponse<CategoryResponse>>()
    val category : LiveData<MyResponse<CategoryResponse>> = _category

    private val _productCartSaved = MutableLiveData<List<ProductItem>>()
    val productCartSaved : LiveData<List<ProductItem>> = _productCartSaved

    private val _productCartSync = MutableLiveData<List<ProductItem>?>()
    val productCartSync : LiveData<List<ProductItem>?> = _productCartSync


    private val accessToken = mainRepository.getAccessToken()

    init {
        collectProductSaved()
        getAllProductSupplier()
        getAllCategorySupplier()
    }

    private fun collectProductSaved() = viewModelScope.launch {
        mainRepository.getProductSaved(OWNER_ID_SUPPLIER).collect {
            _productCartSaved.value = it
            syncServerWithDb(products.value?.data?.result)
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

    private fun getAllCategorySupplier() = viewModelScope.launch {
        accessToken?.let { token ->
            mainRepository.getAllCategorySupplier(token).collect {
                _category.value = it
            }
        }
    }

    private fun getAllProductSupplier() = viewModelScope.launch {
        accessToken?.let { token ->
            mainRepository.getAllProductSupplier(token).collect {
                _products.value = it
                syncServerWithDb(products.value?.data?.result)
            }
        }
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