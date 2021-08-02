package com.pasukanlangit.id.melijo.presentation.mainprovider.home.promo

import androidx.lifecycle.*
import com.pasukanlangit.id.melijo.data.MainRepository
import com.pasukanlangit.id.melijo.data.network.model.request.PromoRequest
import com.pasukanlangit.id.melijo.data.network.model.response.AllPromoResponse
import com.pasukanlangit.id.melijo.utils.MyResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PromoProviderViewModel @Inject constructor(
    private val mainRepository: MainRepository
): ViewModel() {
    private val _promoProvider = MutableLiveData<MyResponse<AllPromoResponse>>()
    val promoProvider: LiveData<MyResponse<AllPromoResponse>> = _promoProvider

    private val accessToken = "Bearer 12|AJm1il583FAaI7PSEFqHLAz87kcOYCoLNlarJXvN"

    init {
        collectPromoProvider()
    }

    fun collectPromoProvider() = viewModelScope.launch {
        accessToken.let { token ->
            mainRepository.getPromoProvider(token).collect { response ->
                _promoProvider.value = response
            }
        }
    }

    fun createPromoProvider(mPromoRequest: PromoRequest) = mainRepository.createPromoProvider(accessToken, mPromoRequest).asLiveData()
    fun updatePromoProvider(promoId: Int, mPromoRequest: PromoRequest) = mainRepository.updatePromoProvider(accessToken, promoId, mPromoRequest).asLiveData()
    fun deletePromoProvider(promoId: Int) = mainRepository.deletePromoProvider(accessToken, promoId).asLiveData()
}