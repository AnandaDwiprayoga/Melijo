package com.pasukanlangit.id.melijo.presentation.main.home.supplier

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pasukanlangit.id.melijo.R
import com.pasukanlangit.id.melijo.databinding.FragmentHomeSupplierBinding
import com.pasukanlangit.id.melijo.presentation.main.MainViewModel
import com.pasukanlangit.id.melijo.presentation.main.checkout.seller.CheckoutSellerActivity
import com.pasukanlangit.id.melijo.presentation.main.home.supplier.product.ProductSupplierViewModel
import com.pasukanlangit.id.melijo.utils.GridSpacingItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SupplierHomeFragment : Fragment(R.layout.fragment_home_supplier){
    private val binding: FragmentHomeSupplierBinding by viewBinding()
    private val viewModel: MainViewModel by viewModels()
    private var cartQty = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            rvMenu.layoutManager = GridLayoutManager(requireContext(),4)
            rvMenu.adapter = MenuAdapter(getDataMenuSupplier())
            rvMenu.addItemDecoration(GridSpacingItemDecoration(8))

            iconCart.setOnClickListener {
                if(cartQty != 0) {
                    Intent(requireContext(), CheckoutSellerActivity::class.java).apply {
                        putExtra(
                            CheckoutSellerActivity.KEY_OWNER_ID,
                            ProductSupplierViewModel.OWNER_ID_SUPPLIER
                        )
                        startActivity(this)
                    }
                }else{
                    Toast.makeText(requireContext(), "Cart Kosong", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        observeCart()
    }

    private fun observeCart() {
        viewModel.cartBuyer.observe(viewLifecycleOwner){
            with(binding){
                cartQty = it.size
                wrapperCart.isVisible = it != null && it.isNotEmpty()
                tvCartQty.text = it.size.toString()
            }
        }
    }
}