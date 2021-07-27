package com.pasukanlangit.id.melijo.presentation.main.home.supplier.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pasukanlangit.id.melijo.data.MainRepository
import com.pasukanlangit.id.melijo.data.network.model.response.AllProductSupplierResponse
import com.pasukanlangit.id.melijo.utils.MyResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductByCategoryViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel(){

    private val _products = MutableLiveData<MyResponse<AllProductSupplierResponse>>()
    val products : LiveData<MyResponse<AllProductSupplierResponse>> = _products

    private val accessToken = mainRepository.getAccessToken()

    fun getProductsByCategory(idCategory: Int) = viewModelScope.launch {
        accessToken?.let { token ->
            mainRepository.getAllProductSupplierByCategory(token, idCategory).collect {
                _products.value = it
            }
        }
    }

}