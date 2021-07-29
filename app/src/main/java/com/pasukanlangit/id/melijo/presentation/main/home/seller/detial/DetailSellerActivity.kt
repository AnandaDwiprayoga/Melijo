package com.pasukanlangit.id.melijo.presentation.main.home.seller.detial

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.pasukanlangit.id.melijo.R
import com.pasukanlangit.id.melijo.data.network.model.response.ProductItem
import com.pasukanlangit.id.melijo.databinding.ActivityDetailSellerBinding
import com.pasukanlangit.id.melijo.presentation.main.checkout.seller.CheckoutSellerActivity
import com.pasukanlangit.id.melijo.utils.MyResponse
import com.pasukanlangit.id.melijo.utils.MyUtils
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("SetTextI18n")
@AndroidEntryPoint
class DetailSellerActivity : AppCompatActivity(R.layout.activity_detail_seller),
    ProductSellerDetailAdapter.ProductItemEvent {

    private val binding: ActivityDetailSellerBinding by viewBinding()
    private val viewModel: DetailSellerViewModel by viewModels()
    private var distanceSeller : Int = 0
    private var ownerId: Int = 0

    private lateinit var adapter : ProductSellerDetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyUtils.setToolbarGreen(window, this)

        binding.btnBack.setOnClickListener { finish() }

        adapter = ProductSellerDetailAdapter(this)

        val idSeller = intent.getIntExtra(KEY_ID_SELLER, -1)
        distanceSeller = intent.getIntExtra(DISTANCE_SELLER, 0)

        val accessToken = viewModel.getAccessToken()

        if(idSeller != -1 && !accessToken.isNullOrEmpty()){
            viewModel.getDetailSeller(accessToken, idSeller)
        }

        binding.wrapperCartFloating.setOnClickListener {
            Intent(this, CheckoutSellerActivity::class.java).apply {
                putExtra(DISTANCE_SELLER, distanceSeller)
                putExtra(CheckoutSellerActivity.KEY_OWNER_ID, ownerId)
                startActivity(this)
            }
        }


        setUpRecyclerProduct()
        observeDetailSeller()
        observeProductCart()
    }


    private fun observeProductCart() {
        viewModel.productCartSaved.observe(this, {
//            binding.wrapperCartFloating.isVisible = !it.isNullOrEmpty()
            with(it.isNullOrEmpty()){
                binding.wrapperCartFloating.isVisible = !this
                if(this) return@observe
            }

            var price = 0
            var qty = 0
            it.forEach { product ->
                price += (product.qty * product.price)
                qty += product.qty
            }
            binding.tvQtyCart.text = "$qty Barang"
            binding.tvPriceCart.text = "Rp $price"
        })
    }

    private fun setUpRecyclerProduct() {
        binding.rvProductSeller.layoutManager = LinearLayoutManager(this)
        binding.rvProductSeller.adapter = adapter
    }

    private fun observeDetailSeller() {
        viewModel.detailSeller.observe(this){
            binding.loading.isVisible = it is MyResponse.Loading
            if(it is MyResponse.Success){
                it.data?.result.let {  detailSellerResponse ->
                    ownerId = detailSellerResponse?.bio?.id ?: 0

                    viewModel.collectProductSaved(ownerId)

                    Glide.with(this).load(detailSellerResponse?.bio?.photo).into(binding.ivSeller)
                    binding.tvRating.text = detailSellerResponse?.bio?.rating.toString()
                    binding.tvNameSeller.text = detailSellerResponse?.bio?.name.toString()
                    binding.tvDistance.text = "${distanceSeller}Km"
                }
            }else if(it is MyResponse.Error){
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.productCartSync.observe(this){
            adapter.submitList(it?.toMutableList())
        }
    }

    companion object {
        const val KEY_ID_SELLER = "KEY_ID_SELLER"
        const val DISTANCE_SELLER = "DISTANCE_SELLER"
    }

    override fun onAddToCart(productItem: ProductItem) {
        viewModel.saveProductToCart(productItem.copy(ownerId = ownerId))
    }

    override fun updateProductCart(productItem: ProductItem) {
        viewModel.updateProductCart(productItem.copy(ownerId = ownerId))
    }

    override fun deleteProductCart(productItem: ProductItem) {
        viewModel.deleteProductCart(productItem.copy(ownerId = ownerId))
    }
}