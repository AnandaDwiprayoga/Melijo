package com.pasukanlangit.id.melijo.presentation.mainprovider

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pasukanlangit.id.melijo.data.MainRepository
import com.pasukanlangit.id.melijo.data.network.model.response.AllProductSupplierResponse
import com.pasukanlangit.id.melijo.data.network.model.response.CategoryResponse
import com.pasukanlangit.id.melijo.data.network.model.response.ProfilProducerResponse
import com.pasukanlangit.id.melijo.utils.MyResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainProviderViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {
    private val accessToken = mainRepository.getAccessToken() ?: ""
    val accountType = mainRepository.getAccountType()

    private val _productsProvider = MutableLiveData<MyResponse<AllProductSupplierResponse>>()
    val productProvider : LiveData<MyResponse<AllProductSupplierResponse>> = _productsProvider

    private val _profileProducer = MutableLiveData<MyResponse<ProfilProducerResponse>>()
    val profileProducer : LiveData<MyResponse<ProfilProducerResponse>> = _profileProducer

    private val _categoryProvider = MutableLiveData<MyResponse<CategoryResponse>>()
    val categoryProvider: LiveData<MyResponse<CategoryResponse>> = _categoryProvider

    init {
        getProfileProducer()
    }

    private fun getProfileProducer() = viewModelScope.launch {
        accessToken.let { token ->
            mainRepository.getProfileProducer(token).collect {
                _profileProducer.value = it
            }
        }
    }

    fun getAllProductsProvider() = viewModelScope.launch {
        accessToken.let { token ->
            mainRepository.getProductsProvider(token).collect {
                _productsProvider.value = it
            }
        }
    }

    fun getCategoryProvider() = viewModelScope.launch {
        accessToken.let { token ->
            mainRepository.getCategoryProvider(token).collect {
                _categoryProvider.value = it
            }
        }
    }
}