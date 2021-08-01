package com.pasukanlangit.id.melijo.presentation.main.checkout.seller

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.github.razir.progressbutton.hideProgress
import com.pasukanlangit.id.melijo.R
import com.pasukanlangit.id.melijo.data.network.model.request.OrderProductsItem
import com.pasukanlangit.id.melijo.data.network.model.request.OrderRequest
import com.pasukanlangit.id.melijo.data.network.model.response.ProductItem
import com.pasukanlangit.id.melijo.databinding.ActivityCheckoutSellerBinding
import com.pasukanlangit.id.melijo.presentation.main.home.seller.detial.DetailSellerActivity
import com.pasukanlangit.id.melijo.presentation.main.home.supplier.product.ProductSupplierViewModel.Companion.OWNER_ID_SUPPLIER
import com.pasukanlangit.id.melijo.presentation.main.promo.AllPromoActivity
import com.pasukanlangit.id.melijo.utils.MyResponse
import com.pasukanlangit.id.melijo.utils.MyUtils
import com.pasukanlangit.id.melijo.utils.setUpToProgressButton
import com.pasukanlangit.id.melijo.utils.showLoading
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("SetTextI18n")
@AndroidEntryPoint
class CheckoutSellerActivity : AppCompatActivity(R.layout.activity_checkout_seller),
    ProductCheckoutAdapter.ProductItemEvent {

    private var productsCartSaved: List<ProductItem>? = null
    private val binding: ActivityCheckoutSellerBinding by viewBinding()
    private val viewModel: CheckoutSellerViewModel by viewModels()
    private var distanceSeller = 0
    private var promoSelected = 0
    private var price = 0
    private var qty = 0

    private var imageProducer : String ?= ""
    private var nameProducer : String ?= ""

    private lateinit var mAdapter: ProductCheckoutAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MyUtils.setToolbarGreen(window, this)
        binding.btnPesan.setUpToProgressButton(this)
        binding.btnBack.setOnClickListener { finish() }
        binding.btnPromo.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    AllPromoActivity::class.java
                )
            )
        }

        distanceSeller = intent.getIntExtra(DetailSellerActivity.DISTANCE_SELLER, 0)

        mAdapter = ProductCheckoutAdapter(emptyList(), this)
        val ownerId = intent.getIntExtra(KEY_OWNER_ID, -1)

        binding.ivBuyer.isVisible = ownerId != OWNER_ID_SUPPLIER
        binding.tvNameBuyer.isVisible = ownerId != OWNER_ID_SUPPLIER

        imageProducer = intent.getStringExtra(KEY_IMG_PRODUCER)
        nameProducer = intent.getStringExtra(KEY_NAME_PRODUCER)

        viewModel.collectProductSaved(ownerId)

        setUpUserData()
        setUpRecyclerCart()
        observeCart()
        observePromoSelected()
        btnOrderClicked()
        observeOrderStatus()
    }

    private fun observeOrderStatus() {
        viewModel.orderResponse.observe(this){
            when (it) {
                is MyResponse.Success -> {
                    binding.btnPesan.hideProgress(getString(R.string.pesan))
                    it.data?.getContentIfNotHandled()?.let {
                        val prefixPromo = StringBuilder("")
                        if (promoSelected != 0) prefixPromo.append("-")

                        Intent(this, PayActivity::class.java).apply {
                            putExtra(PayActivity.KEY_DATA_TRX, it.result)
                            putExtra(
                                PayActivity.KEY_SHIPMENT_PRICE,
                                "Rp ${distanceSeller.times(1000)}"
                            )
                            putExtra(PayActivity.KEY_PROMO_PRICE, "${prefixPromo}Rp $promoSelected")
                            putExtra(PayActivity.KEY_IMG_PRODUCER, imageProducer)
                            putExtra(PayActivity.KEY_NAME_PRODUCER, nameProducer)
                            startActivity(this)
                        }
                    }
                }
                is MyResponse.Loading -> {
                    binding.btnPesan.showLoading()
                }
                is MyResponse.Error -> {
                    binding.btnPesan.hideProgress(getString(R.string.pesan))
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun btnOrderClicked() {
        with(binding) {
            btnPesan.setOnClickListener {
                val address = edtAddressBuyer.text.toString().trim()
                if (address.isNotEmpty()) {
                    if (productsCartSaved != null) {
                        val listProductBuy = mutableListOf<OrderProductsItem>()

                        productsCartSaved?.forEach { productItem ->
                            listProductBuy.add(
                                OrderProductsItem(
                                    productItem.price * productItem.qty,
                                    productItem.qty,
                                    productItem.id
                                )
                            )
                        }
                        val orderRequest = OrderRequest(
                            price.plus(distanceSeller.times(1000)).minus(promoSelected),
                            address,
                            listProductBuy
                        )

                        viewModel.orderTransaction(orderRequest)
                    } else {
                        Toast.makeText(
                            this@CheckoutSellerActivity,
                            "Product kosong",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@CheckoutSellerActivity,
                        "Isi alamat terlebih dahulu",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun observePromoSelected() {
        viewModel.promoMain.observe(this) {
            if (it != null) {
                promoSelected = it.discount
                binding.labelPromo.text = "${it.name} (Rp ${it.discount})"
            } else {
                promoSelected = 0
                binding.labelPromo.text = getString(R.string.daftar_promo)
            }
            val prefixPromo = StringBuilder("")
            if (promoSelected != 0) prefixPromo.append("-")
            binding.tvPricePromo.text = "${prefixPromo}Rp $promoSelected"
            binding.tvPriceTot.text =
                "Rp ${price.plus(distanceSeller.times(1000)).minus(promoSelected)}"
        }
    }

    private fun observeCart() {
        viewModel.productCartSaved.observe(this, {
            price = 0
            qty = 0
            productsCartSaved = it
            it.forEach { product ->
                price += (product.qty * product.price)
                qty += product.qty
            }

            mAdapter.setNewItems(it.toMutableList())

            binding.tvPrice.text = "Rp $price"
            binding.tvPriceTot.text =
                "Rp ${price.plus(distanceSeller.times(1000)).minus(promoSelected)}"
        })
    }

    private fun setUpRecyclerCart() {
        binding.rvProductCheckout.layoutManager = LinearLayoutManager(this)
        binding.rvProductCheckout.adapter = mAdapter
    }

    private fun setUpUserData() {
        Glide.with(this)
            .load(viewModel.imageUser)
            .circleCrop()
            .into(binding.ivBuyer)

        binding.tvNameBuyer.text = viewModel.nameUser
        binding.tvPriceOngkir.text = "Rp ${distanceSeller.times(1000)}"
    }

    override fun updateProductCart(productItem: ProductItem) {
        viewModel.updateProductCart(productItem)
    }

    override fun deleteProductCart(productItem: ProductItem) {
        viewModel.deleteProductCart(productItem)
    }

    companion object {
        const val KEY_OWNER_ID = "KEY_OWNER_ID"
        const val KEY_NAME_PRODUCER = "KEY_NAME_PRODUCER"
        const val KEY_IMG_PRODUCER = "KEY_IMG_PRODUCER"
    }

}