package com.pasukanlangit.id.melijo.presentation.main.home.supplier.product

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pasukanlangit.id.melijo.R
import com.pasukanlangit.id.melijo.databinding.FragmentSupProductWrapperBinding
import com.pasukanlangit.id.melijo.utils.GridSpacingItemDecoration
import com.pasukanlangit.id.melijo.utils.MyResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SupProductWrapperFragment : Fragment(R.layout.fragment_sup_product_wrapper) {
    private val binding: FragmentSupProductWrapperBinding by viewBinding()
    private val viewModel: ProductSupplierViewModel by viewModels()
    private lateinit var mAdapter: ProductSupplierAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter = ProductSupplierAdapter()
        setUpRvProduct()

        viewModel.products.observe(viewLifecycleOwner){
            binding.loading.isVisible = it is MyResponse.Loading
            when(it){
                is MyResponse.Success -> {
                    mAdapter.submitList(it.data?.result?.toMutableList())
                }
                is MyResponse.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }

    private fun setUpRvProduct() {
        with(binding.rvProduct){
            layoutManager = GridLayoutManager(requireContext(), 2)
            this.adapter = mAdapter
            addItemDecoration(GridSpacingItemDecoration(10))
        }
    }
}