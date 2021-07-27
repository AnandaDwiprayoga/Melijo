package com.pasukanlangit.id.melijo.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.pasukanlangit.id.melijo.data.MainRepository
import com.pasukanlangit.id.melijo.presentation.auth.UserType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    fun logout(accessToken: String, userType: UserType) = mainRepository.logout(accessToken,userType).asLiveData()
    fun getAccessToken() : String? = mainRepository.getAccessToken()
    fun getAccountType(): String? = mainRepository.getAccountType()
    fun removeAccessToken() = mainRepository.removeAccessToken()
    fun setSession(value: Boolean) = mainRepository.setSession(value)

    fun getListSeller(token: String) = mainRepository.getListSellerByUser(token).asLiveData()
    fun getUserProfile(token: String) = mainRepository.getProfileUser(token).asLiveData()

}