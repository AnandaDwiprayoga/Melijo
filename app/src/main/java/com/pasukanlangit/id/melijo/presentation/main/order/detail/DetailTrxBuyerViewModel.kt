package com.pasukanlangit.id.melijo.presentation.main.order.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.pasukanlangit.id.melijo.data.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailTrxBuyerViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel(){

    private val token = mainRepository.getAccessToken() ?: ""

    fun getDetailTrxBuyer(trxId: Int) = mainRepository.getTrxDetailBuyer(token, trxId).asLiveData()

    fun cancelTrx(trxId: Int) = mainRepository.cancelTransaction(token,trxId).asLiveData()
}