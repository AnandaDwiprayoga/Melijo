package com.pasukanlangit.id.melijo.presentation.mainprovider.home.homebasesup


import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pasukanlangit.id.melijo.R
import com.pasukanlangit.id.melijo.databinding.FragmentHomeSupProductBinding
import com.pasukanlangit.id.melijo.presentation.main.home.seller.detial.ProductSellerDetailAdapter
import com.pasukanlangit.id.melijo.presentation.main.home.supplier.product.CategorySupplierAdapter
import com.pasukanlangit.id.melijo.presentation.main.home.supplier.product.ProductSupplierAdapter
import com.pasukanlangit.id.melijo.presentation.mainprovider.MainProviderViewModel
import com.pasukanlangit.id.melijo.utils.GridSpacingItemDecoration
import com.pasukanlangit.id.melijo.utils.MyResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeSupProductFragment(private val typeProduct: HomeBaseSupType) : Fragment(R.layout.fragment_home_sup_product) {
    private val binding: FragmentHomeSupProductBinding by viewBinding()
    private val viewModel: MainProviderViewModel by viewModels()

    private lateinit var adapterProduct: ProductBaseSupplierAdapter
    private lateinit var adapterCategory: CategorySupplierAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeProduct()
        observeCategory()
    }

    private fun observeCategory() {
        viewModel.categoryProvider.observe(viewLifecycleOwner){
            binding.loading.isVisible = it is MyResponse.Loading
            when(it){
                is MyResponse.Success -> {
                    adapterCategory.submitList(it.data?.result)
                }
                is MyResponse.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }

    override fun onResume() {
        super.onResume()
        when(typeProduct){
            HomeBaseSupType.TYPE_PRODUCT -> {
                setUpRvProduct()
                viewModel.getAllProductsProvider()
            }
            HomeBaseSupType.TYPE_KATEGORI -> {
                setUpRvCategory()
                viewModel.getCategoryProvider()
            }
            else -> {}
        }
    }

    private fun observeProduct() {
        viewModel.productProvider.observe(viewLifecycleOwner){
            binding.loading.isVisible = it is MyResponse.Loading
            when(it){
                is MyResponse.Success -> {adapterProduct.submitList(it.data?.result) }
                is MyResponse.Error -> { Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show() }
                else -> {}
            }
        }
    }

    private fun setUpRvCategory() {
        adapterCategory = CategorySupplierAdapter()
        with(binding.rv){
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adapterCategory
        }
    }

    private fun setUpRvProduct() {
        adapterProduct = ProductBaseSupplierAdapter()
        with(binding.rv){
            layoutManager = GridLayoutManager(requireContext(), 2)
            this.adapter = adapterProduct
            addItemDecoration(GridSpacingItemDecoration(10))
        }
    }

}