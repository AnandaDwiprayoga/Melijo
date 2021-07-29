package com.pasukanlangit.id.melijo.presentation.main.checkout.seller

import androidx.lifecycle.*
import com.pasukanlangit.id.melijo.data.MainRepository
import com.pasukanlangit.id.melijo.data.network.model.response.ProductItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutSellerViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel(){


    val imageUser = mainRepository.getImageUser()
    val nameUser = mainRepository.getNameUser()

    private val _productCartSaved = MutableLiveData<List<ProductItem>>()
    val productCartSaved : LiveData<List<ProductItem>> = _productCartSaved

    val promoMain = mainRepository.getPromoSelected().asLiveData()

    fun collectProductSaved(ownerId: Int) = viewModelScope.launch {
        mainRepository.getProductSaved(ownerId).collect {
            _productCartSaved.value = it
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