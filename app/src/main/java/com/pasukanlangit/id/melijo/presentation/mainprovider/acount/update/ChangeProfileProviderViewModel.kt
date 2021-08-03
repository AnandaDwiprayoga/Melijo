package com.pasukanlangit.id.melijo.presentation.mainprovider.acount.update

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
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class ChangeProfileProviderViewModel @Inject constructor(private val mainRepository: MainRepository): ViewModel(){

    private val _updatedProfile = MutableLiveData<MyResponse<MetaResponse>>()
    val updatedProfile : LiveData<MyResponse<MetaResponse>> = _updatedProfile

    private val token = mainRepository.getAccessToken() ?: ""

    fun updateProfileProvider(
        name: RequestBody,
        address: RequestBody,
        phoneNumber: RequestBody,
        image: MultipartBody.Part?
    ) = viewModelScope.launch {
        mainRepository.updateProfileProvider(token, name, address, phoneNumber, image).collect {
            _updatedProfile.value = it
        }
    }

}