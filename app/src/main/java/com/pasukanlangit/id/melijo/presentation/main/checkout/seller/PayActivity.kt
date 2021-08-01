package com.pasukanlangit.id.melijo.presentation.main.checkout.seller

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.github.razir.progressbutton.hideProgress
import com.pasukanlangit.id.melijo.R
import com.pasukanlangit.id.melijo.data.network.model.response.DataOrder
import com.pasukanlangit.id.melijo.databinding.ActivityPayBinding
import com.pasukanlangit.id.melijo.utils.MyResponse
import com.pasukanlangit.id.melijo.utils.MyUtils
import com.pasukanlangit.id.melijo.utils.setUpToProgressButton
import com.pasukanlangit.id.melijo.utils.showLoading
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PayActivity : AppCompatActivity(R.layout.activity_pay) {

    private val binding: ActivityPayBinding by viewBinding()
    private val viewModel: PayViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MyUtils.setToolbarGreen(window, this)
        binding.btnBack.setOnClickListener { finish() }

        val promoPrice = intent.getStringExtra(KEY_PROMO_PRICE)
        val shipmentPrice = intent.getStringExtra(KEY_SHIPMENT_PRICE)
        val trxData = intent.getParcelableExtra<DataOrder>(KEY_DATA_TRX)
        val imgProducer = intent.getStringExtra(KEY_IMG_PRODUCER)
        val nameProducer = intent.getStringExtra(KEY_NAME_PRODUCER)

        setUpUI(trxData, promoPrice, shipmentPrice,imgProducer, nameProducer)

        binding.btnCancelPesan.setUpToProgressButton(this)
        binding.btnCancelPesan.setOnClickListener {
            trxData?.let {
                viewModel.cancelTrx(it.id)
            } ?: Toast.makeText(this, "Transaksi tidak bisa dibatalkan", Toast.LENGTH_SHORT).show()
        }

        observeTrxCanceled()
    }

    private fun observeTrxCanceled() {
        viewModel.productCancel.observe(this){
            when(it){
                is MyResponse.Success -> {
                    binding.btnCancelPesan.hideProgress(getString(R.string.batalkan_pesanan))
                    Toast.makeText(this, it.data?.meta?.message, Toast.LENGTH_SHORT).show()
                    finish()
                }
                is MyResponse.Loading -> {
                    binding.btnCancelPesan.showLoading()
                }
                is MyResponse.Error -> {
                    binding.btnCancelPesan.hideProgress(getString(R.string.batalkan_pesanan))
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUpUI(
        trxData: DataOrder?,
        promoPrice: String?,
        shipmentPrice: String?,
        imgProducer: String?,
        nameProducer: String?
    ) {
        with(binding){
            if(imgProducer == null) ivProducer.setImageResource(R.drawable.icon_supplier)
            else  Glide.with(this@PayActivity)
                .load(imgProducer)
                .circleCrop()
                .into(ivProducer)

            var priceProduct = 0

            trxData?.orders?.forEach { prodOrder ->
                priceProduct += prodOrder.total
            }

            tvPrice.text = "Rp $priceProduct"
            tvNameSeller.text = nameProducer ?: "Pesanan diserahkan ke supplier"
            tvPricePromo.text = promoPrice
            tvPriceOngkir.text = shipmentPrice
            tvPriceTot.text = "Rp ${trxData?.totalPay}"
            tvAddressOrder.text = trxData?.buyerAddress
            tvOrderStatus.text = "Status pesanan anda ${trxData?.status}..."
        }
    }

    companion object {
        const val KEY_PROMO_PRICE = "KEY_PROMO_PRICE"
        const val KEY_SHIPMENT_PRICE = "KEY_SHIPMENT_PRICE"
        const val KEY_DATA_TRX = "KEY_DATA_TRX"
        const val KEY_NAME_PRODUCER = "KEY_NAME_PRODUCER"
        const val KEY_IMG_PRODUCER = "KEY_IMG_PRODUCER"
    }
}