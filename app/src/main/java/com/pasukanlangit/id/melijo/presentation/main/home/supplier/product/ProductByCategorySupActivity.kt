package com.pasukanlangit.id.melijo.presentation.main.home.supplier.product

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pasukanlangit.id.melijo.R
import com.pasukanlangit.id.melijo.data.network.model.response.CategoryResultItem
import com.pasukanlangit.id.melijo.databinding.ActivityProductByCategorySupBinding
import com.pasukanlangit.id.melijo.utils.GridSpacingItemDecoration
import com.pasukanlangit.id.melijo.utils.MyResponse
import com.pasukanlangit.id.melijo.utils.MyUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductByCategorySupActivity : AppCompatActivity(R.layout.activity_product_by_category_sup) {

    private val viewModel: ProductByCategoryViewModel by viewModels()
    private val binding: ActivityProductByCategorySupBinding by viewBinding()
    private lateinit var mAdapter: ProductSupplierAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MyUtils.setToolbarGreen(window, this)
        binding.btnBack.setOnClickListener { finish() }

        mAdapter = ProductSupplierAdapter()
        val categoryIntent = intent.getParcelableExtra<CategoryResultItem>(KEY_CATEGORY)

        categoryIntent?.let { category ->
            viewModel.getProductsByCategory(category.id)
            binding.tvNameCategory.text = category.name
        }

        setUpRecyclerView()
        observeProduct()
    }

    private fun setUpRecyclerView() {
        with(binding.rvProduct){
            layoutManager = GridLayoutManager(this@ProductByCategorySupActivity, 2)
            this.adapter = mAdapter
            addItemDecoration(GridSpacingItemDecoration(10))
        }
    }

    private fun observeProduct() {
        viewModel.products.observe(this){
            binding.loading.isVisible = it is MyResponse.Loading
            when(it){
                is MyResponse.Success -> {
                    mAdapter.submitList(it.data?.result?.toMutableList())
                }
                is MyResponse.Error -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }

    companion object {
        const val KEY_CATEGORY = "KEY_CATEGORY"
    }
}