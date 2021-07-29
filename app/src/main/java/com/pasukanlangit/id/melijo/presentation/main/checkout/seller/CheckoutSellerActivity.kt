package com.pasukanlangit.id.melijo.presentation.main.checkout.seller

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.pasukanlangit.id.melijo.R
import com.pasukanlangit.id.melijo.data.network.model.response.ProductItem
import com.pasukanlangit.id.melijo.databinding.ActivityCheckoutSellerBinding
import com.pasukanlangit.id.melijo.presentation.main.home.seller.detial.DetailSellerActivity
import com.pasukanlangit.id.melijo.presentation.main.promo.AllPromoActivity
import com.pasukanlangit.id.melijo.utils.MyUtils
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("SetTextI18n")
@AndroidEntryPoint
class CheckoutSellerActivity : AppCompatActivity(R.layout.activity_checkout_seller), ProductCheckoutAdapter.ProductItemEvent {

    private val binding: ActivityCheckoutSellerBinding by viewBinding()
    private val viewModel: CheckoutSellerViewModel by viewModels()
    private var distanceSeller = 0
    private var promoSelected = 0
    private var price = 0
    private var qty = 0

    private lateinit var mAdapter : ProductCheckoutAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MyUtils.setToolbarGreen(window, this)
        binding.btnBack.setOnClickListener { finish() }
        binding.btnPromo.setOnClickListener { startActivity(Intent(this, AllPromoActivity::class.java)) }
        distanceSeller = intent.getIntExtra(DetailSellerActivity.DISTANCE_SELLER, 0)

        mAdapter = ProductCheckoutAdapter(emptyList(), this)
        val ownerId = intent.getIntExtra(KEY_OWNER_ID, -1)

        viewModel.collectProductSaved(ownerId)

        setUpUserData()
        setUpRecyclerCart()
        observeCart()
        observePromoSelected()
    }

    private fun observePromoSelected() {
        viewModel.promoMain.observe(this){
            if(it != null){
                promoSelected = it.discount
                binding.labelPromo.text = "${it.name} (Rp ${it.discount})"
            }else{
                promoSelected = 0
                binding.labelPromo.text = getString(R.string.daftar_promo)
            }
            val prefixPromo = StringBuilder("")
            if(promoSelected != 0) prefixPromo.append("-")
            binding.tvPricePromo.text = "${prefixPromo}Rp $promoSelected"
            binding.tvPriceTot.text = "Rp ${price.plus(distanceSeller.times(1000)).minus(promoSelected)}"
        }
    }

    private fun observeCart() {
        viewModel.productCartSaved.observe(this, {
            price = 0
            qty = 0
            it.forEach { product ->
                price += (product.qty * product.price)
                qty += product.qty
            }

            mAdapter.setNewItems(it.toMutableList())

            binding.tvPrice.text = "Rp $price"
            binding.tvPriceTot.text = "Rp ${price.plus(distanceSeller.times(1000)).minus(promoSelected)}"
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
    }

}