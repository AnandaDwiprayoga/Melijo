package com.pasukanlangit.id.melijo.presentation.main

import androidx.lifecycle.*
import com.pasukanlangit.id.melijo.data.MainRepository
import com.pasukanlangit.id.melijo.data.network.model.request.UpdateLocationRequest
import com.pasukanlangit.id.melijo.data.network.model.response.OrderBuyerResponse
import com.pasukanlangit.id.melijo.presentation.auth.UserType
import com.pasukanlangit.id.melijo.presentation.main.home.supplier.product.ProductSupplierViewModel.Companion.OWNER_ID_SUPPLIER
import com.pasukanlangit.id.melijo.utils.MyResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _oder = MutableLiveData<MyResponse<OrderBuyerResponse>>()
    val oder : LiveData<MyResponse<OrderBuyerResponse>> = _oder

    private var token = mainRepository.getAccessToken() ?: ""

    val cartBuyer = mainRepository.getProductSaved(OWNER_ID_SUPPLIER).asLiveData()

    fun logout(accessToken: String, userType: UserType) = mainRepository.logout(accessToken,userType).asLiveData()
    fun getAccessToken() : String? = mainRepository.getAccessToken()
    fun getAccountType(): String? = mainRepository.getAccountType()
    fun removeAccessToken() = mainRepository.removeAccessToken()
    fun setSession(value: Boolean) = mainRepository.setSession(value)

    fun getListSeller(token: String) = mainRepository.getListSellerByUser(token).asLiveData()
    fun getUserProfile(token: String) = mainRepository.getProfileUser(token).asLiveData()

    fun setImageUser(photo: String) = mainRepository.setImageUser(photo)
    fun setNameUser(name: String) = mainRepository.setNameUser(name)

    fun saveLocationUser(updateLocationRequest: UpdateLocationRequest) = mainRepository.setLocationUser(updateLocationRequest)
    fun getLocationUser() = mainRepository.getLocationUser()

    fun updateLocation(updateLocationRequest: UpdateLocationRequest) = mainRepository.updateLocationUser(token, updateLocationRequest).asLiveData()

    fun getOrderTransaction() = viewModelScope.launch {
        getAccessToken()?.let { accessToken ->
            mainRepository.getTransactionForBuyer(accessToken).collect {
                _oder.value = it
            }
        }
    }


}