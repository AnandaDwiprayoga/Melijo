package com.pasukanlangit.id.melijo.presentation.main.account.update

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
class ChangeProfileUserViewModel @Inject constructor(private val mainRepository: MainRepository): ViewModel(){

    private val _updatedProfile = MutableLiveData<MyResponse<MetaResponse>>()
    val updatedProfile : LiveData<MyResponse<MetaResponse>> = _updatedProfile

    private val token = mainRepository.getAccessToken() ?: ""

    fun updateProfileUser(
        name: RequestBody,
        email: RequestBody,
        address: RequestBody,
        phoneNumber: RequestBody,
        image: MultipartBody.Part?
    ) = viewModelScope.launch {
        mainRepository.updateProfileUser(token, name, email, address, phoneNumber, image).collect {
            _updatedProfile.value = it
        }
    }

}