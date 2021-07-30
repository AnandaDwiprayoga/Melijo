package com.pasukanlangit.id.melijo.presentation.main.checkout.seller

import androidx.lifecycle.*
import com.pasukanlangit.id.melijo.data.MainRepository
import com.pasukanlangit.id.melijo.data.network.model.request.OrderRequest
import com.pasukanlangit.id.melijo.data.network.model.response.MetaResponse
import com.pasukanlangit.id.melijo.data.network.model.response.OrderResponse
import com.pasukanlangit.id.melijo.data.network.model.response.ProductItem
import com.pasukanlangit.id.melijo.utils.MyResponse
import com.pasukanlangit.id.melijo.utils.SingleEvent
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

    private val _orderResponse = MutableLiveData<MyResponse<SingleEvent<OrderResponse>>>()
    val orderResponse : LiveData<MyResponse<SingleEvent<OrderResponse>>> = _orderResponse

    val promoMain = mainRepository.getPromoSelected().asLiveData()

    private val accessToken = mainRepository.getAccessToken() ?: ""

    fun collectProductSaved(ownerId: Int) = viewModelScope.launch {
        mainRepository.getProductSaved(ownerId).collect {
            _productCartSaved.value = it
        }
    }

    fun orderTransaction(orderRequest: OrderRequest) = viewModelScope.launch {
        mainRepository.createTransactionForBuyer(accessToken, orderRequest).collect {
            _orderResponse.value = it
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