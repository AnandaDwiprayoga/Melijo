package com.pasukanlangit.id.melijo.presentation.main.home.supplier.product

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pasukanlangit.id.melijo.R
import com.pasukanlangit.id.melijo.databinding.FragmentSupCategoryWrapperBinding
import com.pasukanlangit.id.melijo.utils.MyResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SupCategoryWrapperFragment : Fragment(R.layout.fragment_sup_category_wrapper) {

    private val binding: FragmentSupCategoryWrapperBinding by viewBinding()
    private val viewModel: ProductSupplierViewModel by viewModels()
    private lateinit var mAdapter: CategorySupplierAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter = CategorySupplierAdapter()

        setUpRecyclerviewCategory()
        observeCategory()

    }

    private fun observeCategory() {
        viewModel.category.observe(viewLifecycleOwner){
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

    private fun setUpRecyclerviewCategory() {
        with(binding.rvCategory){
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mAdapter
        }
    }
}