package com.pasukanlangit.id.melijo.presentation.mainprovider.order.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.pasukanlangit.id.melijo.R
import com.pasukanlangit.id.melijo.data.network.model.response.DetailTrxResult
import com.pasukanlangit.id.melijo.databinding.ActivityDetailTrxProducerBinding
import com.pasukanlangit.id.melijo.utils.MyResponse
import com.pasukanlangit.id.melijo.utils.MyUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailTrxProducerActivity : AppCompatActivity(R.layout.activity_detail_trx_producer) {

    private lateinit var mAdapter: ProductDetailTrxAdapter
    private val binding: ActivityDetailTrxProducerBinding by viewBinding()
    private val viewModel: DetailTrxProducerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MyUtils.setToolbarGreen(window, this)
        binding.btnBack.setOnClickListener { finish() }

        val idTransaction = intent.getIntExtra(KEY_TRX_ID, -1)

        setUpRvProduct()
        observeDetailTrx(idTransaction)
    }

    private fun setUpRvProduct() {
        mAdapter = ProductDetailTrxAdapter()
        with(binding.rvProduct){
            layoutManager = LinearLayoutManager(this@DetailTrxProducerActivity)
            adapter = mAdapter
        }
    }

    private fun observeDetailTrx(idTransaction: Int) {
        viewModel.getDetailTrxProducer(idTransaction).observe(this){
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

    private fun setUpUI(data: DetailTrxResult) {
        with(binding){
            Glide.with(this@DetailTrxProducerActivity)
                .load(data.photo)
                .circleCrop()
                .into(ivBuyer)

            tvNameBuyer.text = data.name
            tvAddressOrder.text = data.address
            tvPriceTot.text = "Rp ${data.total}"
        }
    }

    companion object {
        const val KEY_TRX_ID = "KEY_TRX_ID"
    }
}