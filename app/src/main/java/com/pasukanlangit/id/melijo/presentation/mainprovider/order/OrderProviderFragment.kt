package com.pasukanlangit.id.melijo.presentation.mainprovider.order

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pasukanlangit.id.melijo.R
import com.pasukanlangit.id.melijo.data.network.model.request.UpdateTransactionRequest
import com.pasukanlangit.id.melijo.databinding.FragmentOrderProviderBinding
import com.pasukanlangit.id.melijo.presentation.mainprovider.MainProviderViewModel
import com.pasukanlangit.id.melijo.utils.MyResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderProviderFragment : Fragment(R.layout.fragment_order_provider) {
    private lateinit var mAdapter: TransactionProducerAdapter
    private val binding: FragmentOrderProviderBinding by viewBinding()
    private val viewModel: MainProviderViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getTransactionForProducer()

        with(binding){
            rbStatusAll.setOnClickListener {
                viewModel.setStatusTransaction(null)
            }
            rbStatusInProcess.setOnClickListener {
                viewModel.setStatusTransaction(StatusTransaction.IN_PROCESS.value)
            }
            rbStatusDone.setOnClickListener {
                viewModel.setStatusTransaction(StatusTransaction.DONE.value)
            }
        }

        setUpRvTrx()
        observeStatusTransaction()
        observeTransactionUpdate()
        observeTrx()
    }

    private fun observeTransactionUpdate() {
        viewModel.updateTrx.observe(viewLifecycleOwner){
            binding.loading.isVisible = it is MyResponse.Loading
            when(it){
                is MyResponse.Success -> {
                    it.data?.getContentIfNotHandled()?.let { response ->
                        Toast.makeText(requireContext(), response.meta.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is MyResponse.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }

    private fun observeStatusTransaction() {
        viewModel.statusTransaction.observe(viewLifecycleOwner){
            viewModel.getTransactionForProducer()
        }
    }

    private fun setUpRvTrx() {
        mAdapter = TransactionProducerAdapter{ indexStatus: Int, transactionId: Int ->
            viewModel.updateTrx(transactionId, UpdateTransactionRequest(
                status = StatusTransaction.values()[indexStatus].value
            ))
        }

        with(binding.rvTrx){
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mAdapter
        }
    }

    private fun observeTrx() {
        viewModel.trx.observe(viewLifecycleOwner){
            binding.loading.isVisible = it is MyResponse.Loading
            when(it){
                is MyResponse.Success -> {
                    it.data?.result?.let { transactions ->
                        mAdapter.submitList(transactions)
                    }
                }
                is MyResponse.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }
}