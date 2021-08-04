package com.pasukanlangit.id.melijo.presentation.mainprovider.order.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.pasukanlangit.id.melijo.data.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailTrxProducerViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {
    private val token = mainRepository.getAccessToken() ?: ""

    fun getDetailTrxProducer(trxId: Int) = mainRepository.getTrxDetailProducer(token, trxId).asLiveData()
}