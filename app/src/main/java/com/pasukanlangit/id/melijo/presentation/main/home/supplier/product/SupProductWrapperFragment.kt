package com.pasukanlangit.id.melijo.presentation.main.home.supplier.product

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pasukanlangit.id.melijo.R
import com.pasukanlangit.id.melijo.data.network.model.response.ProductItem
import com.pasukanlangit.id.melijo.databinding.FragmentSupProductWrapperBinding
import com.pasukanlangit.id.melijo.presentation.main.home.seller.detial.ProductSellerDetailAdapter
import com.pasukanlangit.id.melijo.presentation.main.home.supplier.product.ProductSupplierViewModel.Companion.OWNER_ID_SUPPLIER
import com.pasukanlangit.id.melijo.utils.GridSpacingItemDecoration
import com.pasukanlangit.id.melijo.utils.MyResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SupProductWrapperFragment : Fragment(R.layout.fragment_sup_product_wrapper),
    ProductSellerDetailAdapter.ProductItemEvent {
    private val binding: FragmentSupProductWrapperBinding by viewBinding()
    private val viewModel: ProductSupplierViewModel by viewModels()
    private lateinit var mAdapter: ProductSupplierAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter = ProductSupplierAdapter(this)
        setUpRvProduct()

        viewModel.products.observe(viewLifecycleOwner){
            binding.loading.isVisible = it is MyResponse.Loading
            when(it){
                is MyResponse.Success -> { }
                is MyResponse.Error -> { Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show() }
                else -> {}
            }
        }

        observeProductCart()
    }

    private fun observeProductCart() {
        viewModel.productCartSaved.observe(viewLifecycleOwner, {
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

        viewModel.productCartSync.observe(viewLifecycleOwner){
            mAdapter.submitList(it?.toMutableList())
        }
    }

    private fun setUpRvProduct() {
        with(binding.rvProduct){
            layoutManager = GridLayoutManager(requireContext(), 2)
            this.adapter = mAdapter
            addItemDecoration(GridSpacingItemDecoration(10))
        }
    }

    override fun onAddToCart(productItem: ProductItem) {
        viewModel.saveProductToCart(productItem.copy(ownerId = OWNER_ID_SUPPLIER))
    }

    override fun updateProductCart(productItem: ProductItem) {
        viewModel.updateProductCart(productItem.copy(ownerId = OWNER_ID_SUPPLIER))
    }

    override fun deleteProductCart(productItem: ProductItem) {
        viewModel.deleteProductCart(productItem.copy(ownerId = OWNER_ID_SUPPLIER))
    }
}