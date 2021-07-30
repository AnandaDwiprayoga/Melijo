package com.pasukanlangit.id.melijo.presentation.mainprovider.home.category

import androidx.lifecycle.*
import com.pasukanlangit.id.melijo.data.MainRepository
import com.pasukanlangit.id.melijo.data.network.model.request.CategoryRequest
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

    fun createCategoryProvider(mCategoryRequest: CategoryRequest) = mainRepository.createCategory(accessToken, mCategoryRequest).asLiveData()

    fun updateCategoryProvider(categoryId: Int, mCategoryRequest: CategoryRequest) = mainRepository.updateCategory(accessToken, categoryId, mCategoryRequest).asLiveData()

    fun deleteCategoryProvider(categoryId: Int) = mainRepository.deleteCategory(accessToken, categoryId).asLiveData()
}