package com.pasukanlangit.id.melijo.presentation.main.order.detail

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pasukanlangit.id.melijo.R
import com.pasukanlangit.id.melijo.data.network.model.response.DetailTrxBuyerResult
import com.pasukanlangit.id.melijo.databinding.ActivityDetailTrxBuyerBinding
import com.pasukanlangit.id.melijo.presentation.mainprovider.order.StatusTransaction
import com.pasukanlangit.id.melijo.presentation.mainprovider.order.detail.ProductDetailTrxAdapter
import com.pasukanlangit.id.melijo.utils.MyResponse
import com.pasukanlangit.id.melijo.utils.MyUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailTrxBuyerActivity : AppCompatActivity(R.layout.activity_detail_trx_buyer) {
    private lateinit var mAdapter: ProductDetailTrxAdapter
    private val binding: ActivityDetailTrxBuyerBinding by viewBinding()

    private val viewModel: DetailTrxBuyerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MyUtils.setToolbarGreen(window, this)
        val idTransaction = intent.getIntExtra(KEY_TRX_ID, -1)

        binding.btnBack.setOnClickListener { finish() }
        binding.btnCancelPesan.setOnClickListener {
            viewModel.cancelTrx(idTransaction).observe(this){
                binding.loading.isVisible = it is MyResponse.Loading
                when(it){
                    is MyResponse.Success -> {
                        it.data?.meta?.let { data ->
                            Toast.makeText(this, data.message, Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                    is MyResponse.Error -> { Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show() }
                    else -> {}
                }
            }
        }


        setUpRvProduct()
        observeDetailTrx(idTransaction)
    }

    private fun observeDetailTrx(idTransaction: Int) {
        viewModel.getDetailTrxBuyer(idTransaction).observe(this){
            binding.loading.isVisible = it is MyResponse.Loading
            when(it){
                is MyResponse.Success -> {
                    it.data?.result?.let { data ->
                        setUpUI(data)
                        mAdapter.submitList(data.orders)
                    }
                }
                is MyResponse.Error -> { Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show() }
                else -> {}
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUpUI(data: DetailTrxBuyerResult) {
        with(binding){
            tvAddressOrder.text = data.address
            tvPriceTot.text = "Rp ${data.total}"
            tvStatus.text = data.status

            btnCancelPesan.isVisible = data.status.equals(StatusTransaction.PENDING.value, true)
        }
    }

    private fun setUpRvProduct() {
        mAdapter = ProductDetailTrxAdapter()
        with(binding.rvProduct){
            layoutManager = LinearLayoutManager(this@DetailTrxBuyerActivity)
            adapter = mAdapter
        }
    }

    companion object {
        const val KEY_TRX_ID = "KEY_TRX_ID"
    }
}