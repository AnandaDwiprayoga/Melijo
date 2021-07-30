package com.pasukanlangit.id.melijo.presentation.main.checkout.seller

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pasukanlangit.id.melijo.data.MainRepository
import com.pasukanlangit.id.melijo.data.network.model.response.MetaResponse
import com.pasukanlangit.id.melijo.utils.MyResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PayViewModel @Inject constructor(private val mainRepository: MainRepository): ViewModel(){
    private val _productCancel = MutableLiveData<MyResponse<MetaResponse>>()
    val productCancel : LiveData<MyResponse<MetaResponse>> = _productCancel

    private var accessToken  = mainRepository.getAccessToken() ?: ""

    fun cancelTrx(trxId: Int) = viewModelScope.launch {
        mainRepository.cancelTransaction(accessToken, trxId).collect {
            _productCancel.value = it
        }
    }
}