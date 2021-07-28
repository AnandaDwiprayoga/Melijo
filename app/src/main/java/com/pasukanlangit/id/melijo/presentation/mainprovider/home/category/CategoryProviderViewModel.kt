package com.pasukanlangit.id.melijo.presentation.mainprovider.home.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pasukanlangit.id.melijo.data.MainRepository
import com.pasukanlangit.id.melijo.data.network.model.response.CategoryResponse
import com.pasukanlangit.id.melijo.utils.MyResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryProviderViewModel @Inject constructor(
    private val mainRepository: MainRepository
): ViewModel() {
    private val _categoryProvider = MutableLiveData<MyResponse<CategoryResponse>>()
    val categoryProvider: LiveData<MyResponse<CategoryResponse>> = _categoryProvider

    private val accessToken = "Bearer 12|AJm1il583FAaI7PSEFqHLAz87kcOYCoLNlarJXvN"

    init {
        getCategoryProvider()
    }

    private fun getCategoryProvider() = viewModelScope.launch {
        accessToken.let { token ->
            mainRepository.getCategoryProvider(token).collect {
                _categoryProvider.value = it
            }
        }
    }
}