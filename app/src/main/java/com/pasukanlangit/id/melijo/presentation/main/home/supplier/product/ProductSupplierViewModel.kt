package com.pasukanlangit.id.melijo.presentation.main.home.supplier.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pasukanlangit.id.melijo.data.MainRepository
import com.pasukanlangit.id.melijo.data.network.model.response.AllProductSupplierResponse
import com.pasukanlangit.id.melijo.data.network.model.response.CategoryResponse
import com.pasukanlangit.id.melijo.utils.MyResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductSupplierViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {
    private val _products = MutableLiveData<MyResponse<AllProductSupplierResponse>>()
    val products : LiveData<MyResponse<AllProductSupplierResponse>> = _products

    private val _category = MutableLiveData<MyResponse<CategoryResponse>>()
    val category : LiveData<MyResponse<CategoryResponse>> = _category

    private val accessToken = mainRepository.getAccessToken()

    init {
        getAllProductSupplier()
        getAllCategorySupplier()
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
            }
        }

    }

}