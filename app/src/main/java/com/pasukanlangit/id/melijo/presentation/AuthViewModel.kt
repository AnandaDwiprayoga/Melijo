package com.pasukanlangit.id.melijo.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.pasukanlangit.id.melijo.data.MainRepository
import com.pasukanlangit.id.melijo.data.network.model.request.LoginRequest
import com.pasukanlangit.id.melijo.data.network.model.request.RegisterRequest
import com.pasukanlangit.id.melijo.presentation.auth.UserType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    val accountType = mainRepository.getAccountType()
    fun checkKeyIsValid(key: String) = mainRepository.checkKeyIsValid(key).asLiveData()
    fun loginUser(loginRequest: LoginRequest) = mainRepository.loginUser(loginRequest).asLiveData()
    fun loginProducer(loginRequest: LoginRequest) = mainRepository.loginProducer(loginRequest).asLiveData()
    fun register(registerRequest: RegisterRequest, userType: UserType) = mainRepository.register(registerRequest, userType).asLiveData()
    fun setAccessToken(token: String) = mainRepository.setAccessToken(token)
    fun setSession(value :Boolean) = mainRepository.setSession(value)
    fun getSession() :Boolean = mainRepository.getSession()
    fun setAccountType(accountType: String) = mainRepository.setAccountType(accountType)
    fun saveImageUser(photo: String) {
        mainRepository.setImageUser(photo)
    }

    fun saveNameUser(name: String) {
        mainRepository.setNameUser(name)
    }
}