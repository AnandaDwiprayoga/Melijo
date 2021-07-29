package com.pasukanlangit.id.melijo.presentation.main.promo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pasukanlangit.id.melijo.data.MainRepository
import com.pasukanlangit.id.melijo.data.network.model.response.AllPromoResponse
import com.pasukanlangit.id.melijo.data.network.model.response.PromoResultItem
import com.pasukanlangit.id.melijo.presentation.auth.UserType
import com.pasukanlangit.id.melijo.utils.MyResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllPromoViewModel @Inject constructor(private val mainRepository: MainRepository): ViewModel() {
    private val _promo = MutableLiveData<MyResponse<AllPromoResponse>>()
    val promo : LiveData<MyResponse<AllPromoResponse>> = _promo

    private val accessToken = mainRepository.getAccessToken() ?: ""

    init {
        getAllPromoAvailableForUser()
    }

    private fun getAllPromoAvailableForUser() = viewModelScope.launch {
        mainRepository.getAllPromoForUser(accessToken, UserType.TYPE_SUPPLIER.value).collect {
            _promo.value = it
        }
    }

    fun insertPromoSelected(promoResultItem: PromoResultItem) = viewModelScope.launch {
        mainRepository.insertPromo(promoResultItem)
    }

    fun deletePromoSelected() = viewModelScope.launch {
        mainRepository.deletePromo()
    }
}