package com.pasukanlangit.id.melijo.presentation.main.checkout.seller

import android.annotation.SuppressLint
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
import com.pasukanlangit.id.melijo.presentation.main.home.seller.detial.ProductSellerDetailAdapter
import com.pasukanlangit.id.melijo.utils.MyUtils
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("SetTextI18n")
@AndroidEntryPoint
class CheckoutSellerActivity : AppCompatActivity(R.layout.activity_checkout_seller),
    ProductSellerDetailAdapter.ProductItemEvent {

    private val binding: ActivityCheckoutSellerBinding by viewBinding()
    private val viewModel: CheckoutSellerViewModel by viewModels()
    private var distanceSeller = 0.0

    private lateinit var mAdapter : ProductSellerDetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MyUtils.setToolbarGreen(window, this)
        binding.btnBack.setOnClickListener { finish() }
        distanceSeller = intent.getStringExtra(DetailSellerActivity.DISTANCE_SELLER)?.replace(",", ".")?.toDouble() ?: 0.0

        mAdapter = ProductSellerDetailAdapter(this)

        setUpUserData()
        setUpRecyclerCart()
        observeCart()
    }

    private fun observeCart() {
        viewModel.productCartSaved.observe(this, {
            var price = 0
            var qty = 0
            it.forEach { product ->
                price += (product.qty * product.price)
                qty += product.qty
            }

            mAdapter.submitList(it.toMutableList())
            mAdapter.notifyDataSetChanged()

            binding.tvPrice.text = "Rp $price"
            binding.tvPriceTot.text = "Rp ${qty.plus(distanceSeller.times(1000))}"
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

    override fun onAddToCart(productItem: ProductItem) {
        viewModel.saveProductToCart(productItem)
    }

    override fun updateProductCart(productItem: ProductItem) {
        viewModel.updateProductCart(productItem)
    }

    override fun deleteProductCart(productItem: ProductItem) {
        viewModel.deleteProductCart(productItem)
    }

}