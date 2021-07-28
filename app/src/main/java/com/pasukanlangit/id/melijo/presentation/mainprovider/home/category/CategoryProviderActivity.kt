package com.pasukanlangit.id.melijo.presentation.mainprovider.home.category

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pasukanlangit.id.melijo.R
import com.pasukanlangit.id.melijo.databinding.ActivityCategoryProviderBinding
import com.pasukanlangit.id.melijo.utils.MyResponse
import com.pasukanlangit.id.melijo.utils.MyUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryProviderActivity : AppCompatActivity(R.layout.activity_category_provider) {

    private val binding: ActivityCategoryProviderBinding by viewBinding()
    private val viewModel: CategoryProviderViewModel by viewModels()
    private lateinit var mAdapter: CategoryProviderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MyUtils.setToolbarGreen(window, this)
        mAdapter = CategoryProviderAdapter()
        setupCategory()
        viewModel.categoryProvider.observe(this, { response ->
            binding.loading.isVisible = response is MyResponse.Loading
            when(response) {
                is MyResponse.Success -> mAdapter.setCategoryProvider(response.data?.result)
                is  MyResponse.Error -> Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                else -> {}
            }
        })
    }

    private fun setupCategory() {
        with(binding.rvCategory) {
            layoutManager = LinearLayoutManager(this@CategoryProviderActivity)
            adapter = mAdapter
        }
    }
}