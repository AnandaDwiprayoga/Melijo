package com.pasukanlangit.id.melijo.presentation.main.order

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pasukanlangit.id.melijo.R
import com.pasukanlangit.id.melijo.databinding.FragmentOrderBinding
import com.pasukanlangit.id.melijo.presentation.main.MainViewModel
import com.pasukanlangit.id.melijo.utils.MyResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderFragment : Fragment(R.layout.fragment_order){
    private val binding: FragmentOrderBinding by viewBinding()
    private val viewModel: MainViewModel by viewModels()
    private lateinit var mAdapter : OrderBuyerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getOrderTransaction()
        setUpRv()
        observeOrderTransaction()
    }

    private fun setUpRv() {
        mAdapter = OrderBuyerAdapter()
        binding.rvOrder.layoutManager = LinearLayoutManager(requireContext())
        binding.rvOrder.adapter = mAdapter
    }

    private fun observeOrderTransaction() {
        viewModel.oder.observe(viewLifecycleOwner){
            binding.loading.isVisible = it is MyResponse.Loading
            when (it) {
                is MyResponse.Success -> {
                    it.data?.result?.let { order ->
                        mAdapter.submitList(order)
                    }
                }
                is MyResponse.Loading -> { }
                is MyResponse.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}