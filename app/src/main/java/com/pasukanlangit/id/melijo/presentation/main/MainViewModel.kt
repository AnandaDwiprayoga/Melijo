package com.pasukanlangit.id.melijo.presentation.main

import androidx.lifecycle.*
import com.pasukanlangit.id.melijo.data.MainRepository
import com.pasukanlangit.id.melijo.data.network.model.response.OrderBuyerResponse
import com.pasukanlangit.id.melijo.presentation.auth.UserType
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

    fun logout(accessToken: String, userType: UserType) = mainRepository.logout(accessToken,userType).asLiveData()
    fun getAccessToken() : String? = mainRepository.getAccessToken()
    fun getAccountType(): String? = mainRepository.getAccountType()
    fun removeAccessToken() = mainRepository.removeAccessToken()
    fun setSession(value: Boolean) = mainRepository.setSession(value)

    fun getListSeller(token: String) = mainRepository.getListSellerByUser(token).asLiveData()
    fun getUserProfile(token: String) = mainRepository.getProfileUser(token).asLiveData()

    fun setImageUser(photo: String) = mainRepository.setImageUser(photo)
    fun setNameUser(name: String) = mainRepository.setNameUser(name)

    fun getOrderTransaction() = viewModelScope.launch {
        getAccessToken()?.let { accessToken ->
            mainRepository.getTransactionForBuyer(accessToken).collect {
                _oder.value = it
            }
        }
    }
}