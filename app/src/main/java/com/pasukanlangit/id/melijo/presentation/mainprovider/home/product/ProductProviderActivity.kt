package com.pasukanlangit.id.melijo.presentation.mainprovider.home.product

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pasukanlangit.id.melijo.R
import com.pasukanlangit.id.melijo.databinding.ActivityProductProviderBinding
import com.pasukanlangit.id.melijo.utils.MyResponse
import com.pasukanlangit.id.melijo.utils.MyUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductProviderActivity : AppCompatActivity(R.layout.activity_product_provider) {

    private val binding: ActivityProductProviderBinding by viewBinding()
    private val viewModel: ProductProviderViewModel by viewModels()
    private lateinit var adapter: ProductProviderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MyUtils.setToolbarGreen(window, this)
        adapter = ProductProviderAdapter()
        setupProducts()
        viewModel.productProvider.observe(this, { response ->
            binding.loading.isVisible = response is MyResponse.Loading
            when(response) {
                is MyResponse.Success -> adapter.setProductProvider(response.data?.result)
                is MyResponse.Error -> Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                else -> {}
            }
        })
    }

    private fun setupProducts() {
        with(binding.rvProduct) {
            layoutManager = LinearLayoutManager(this@ProductProviderActivity)
            adapter = this@ProductProviderActivity.adapter
        }
    }
}