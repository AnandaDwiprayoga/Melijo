package com.pasukanlangit.id.melijo.presentation.mainprovider

import androidx.lifecycle.*
import com.pasukanlangit.id.melijo.data.MainRepository
import com.pasukanlangit.id.melijo.data.network.model.request.UpdateTransactionRequest
import com.pasukanlangit.id.melijo.data.network.model.response.*
import com.pasukanlangit.id.melijo.presentation.auth.UserType
import com.pasukanlangit.id.melijo.utils.MyResponse
import com.pasukanlangit.id.melijo.utils.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainProviderViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {
    private val accessToken = mainRepository.getAccessToken() ?: ""
    val accountType = mainRepository.getAccountType()

    private val _productsProvider = MutableLiveData<MyResponse<AllProductSupplierResponse>>()
    val productProvider : LiveData<MyResponse<AllProductSupplierResponse>> = _productsProvider

    private val _profileProducer = MutableLiveData<MyResponse<ProfilProducerResponse>>()
    val profileProducer : LiveData<MyResponse<ProfilProducerResponse>> = _profileProducer

    private val _categoryProvider = MutableLiveData<MyResponse<CategoryResponse>>()
    val categoryProvider: LiveData<MyResponse<CategoryResponse>> = _categoryProvider

    private val _promoProvider = MutableLiveData<MyResponse<AllPromoResponse>>()
    val promoProvider: LiveData<MyResponse<AllPromoResponse>> = _promoProvider

    private val _trx = MutableLiveData<MyResponse<TransactionProducerResponse>>()
    val trx: LiveData<MyResponse<TransactionProducerResponse>> = _trx

    private val _updateTrx = MutableLiveData<MyResponse<SingleEvent<MetaResponse>>>()
    val updateTrx: LiveData<MyResponse<SingleEvent<MetaResponse>>> = _updateTrx

    private val _allUsers = MutableLiveData<MyResponse<AllUserForProducerResponse>>()
    val allUsers: LiveData<MyResponse<AllUserForProducerResponse>> = _allUsers

    private val _statusTransaction = MutableLiveData<String?>()
    val statusTransaction: LiveData<String?> = _statusTransaction

    init {
        getProfileProducer()
    }

    fun setStatusTransaction(status: String?){
        _statusTransaction.value = status
    }

    fun getTransactionForProducer() = viewModelScope.launch {
        accessToken.let { token ->
            mainRepository.getTransactionByStatusProducer(token, statusTransaction.value).collect {
                _trx.value = it
            }
        }
    }

    fun updateTrx(transactionId: Int, transactionRequest: UpdateTransactionRequest) = viewModelScope.launch {
        accessToken.let { token ->
            mainRepository.updateTransactionStatusProducer(token,transactionId, transactionRequest).collect {
                _updateTrx.value = it
                getTransactionForProducer()
            }
        }
    }

    fun getProfileProducer() = viewModelScope.launch {
        accessToken.let { token ->
            mainRepository.getProfileProducer(token).collect {
                _profileProducer.value = it
            }
        }
    }

    fun getPromoProvider() = viewModelScope.launch {
        accessToken.let { token ->
            mainRepository.getPromoProvider(token).collect { response ->
                _promoProvider.value = response
            }
        }
    }

    fun getAllProductsProvider() = viewModelScope.launch {
        accessToken.let { token ->
            mainRepository.getProductsProvider(token).collect {
                _productsProvider.value = it
            }
        }
    }

    fun getAllUserForProducer() = viewModelScope.launch {
        accessToken.let { token ->
            mainRepository.getAllUsersForProducer(token).collect {
                _allUsers.value = it
            }
        }
    }

    fun getCategoryProvider() = viewModelScope.launch {
        accessToken.let { token ->
            mainRepository.getCategoryProvider(token).collect {
                _categoryProvider.value = it
            }
        }
    }

    fun logout() = mainRepository.logout(accessToken,UserType.TYPE_SUPPLIER).asLiveData()
    fun toggleStatus() = mainRepository.toggleStatusProducer(accessToken).asLiveData()

    fun removeAccessToken() = mainRepository.removeAccessToken()
    fun setSession(value: Boolean) = mainRepository.setSession(value)

}