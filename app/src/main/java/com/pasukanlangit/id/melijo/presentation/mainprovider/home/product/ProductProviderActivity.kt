package com.pasukanlangit.id.melijo.presentation.mainprovider.home.product

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pasukanlangit.id.melijo.R
import com.pasukanlangit.id.melijo.data.network.model.response.ProductItem
import com.pasukanlangit.id.melijo.databinding.ActivityProductProviderBinding
import com.pasukanlangit.id.melijo.utils.MyResponse
import com.pasukanlangit.id.melijo.utils.MyUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductProviderActivity : AppCompatActivity(R.layout.activity_product_provider) {

    private val binding: ActivityProductProviderBinding by viewBinding()
    private val viewModel: ProductProviderViewModel by viewModels()
    private lateinit var adapter: ProductProviderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MyUtils.setToolbarGreen(window, this)
        adapter = ProductProviderAdapter()
        setupProducts()
        binding.btnBack.setOnClickListener { finish() }
        binding.buttonAddProduct.setOnClickListener { startActivity(Intent(this, AddUpdateProductActivity::class.java)) }
        adapter.setOnEditButtonClickLister(object: ProductProviderAdapter.OnEditButtonProduct {
            override fun onEditButtonClickListener(item: ProductItem) {
                val intent = Intent(this@ProductProviderActivity, AddUpdateProductActivity::class.java)
                intent.putExtra(AddUpdateProductActivity.KEY_UPDATE_PRODUCT, item)
                startActivity(intent)
            }
        })
        adapter.setOnDeleteButtonClickListener(object: ProductProviderAdapter.OnDeleteButtonProduct {
            override fun onDeleteButtonClickListener(id: Int) {
                val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this@ProductProviderActivity)
                alertDialog.setCancelable(false)
                alertDialog.setTitle("Konfirmasi Hapus Barang Jualan")
                alertDialog.setMessage("Apakah anda yakin menghapus barang ini?")
                alertDialog.setPositiveButton("Yakin") { dialog: DialogInterface, _ ->
                    deleteProduct(id)
                    dialog.dismiss()
                }
                alertDialog.setNegativeButton("Batal") { dialog: DialogInterface, _ ->
                    dialog.dismiss()
                }
                alertDialog.show()
            }
        })
    }

    private fun setupProducts() {
        with(binding.rvProduct) {
            layoutManager = LinearLayoutManager(this@ProductProviderActivity)
            adapter = this@ProductProviderActivity.adapter
        }

        viewModel.productProvider.observe(this, { response ->
            binding.loading.isVisible = response is MyResponse.Loading
            when(response) {
                is MyResponse.Success -> {
                    response.data?.result?.let { data ->
                        adapter.setProductProvider(data)
                        adapter.notifyDataSetChanged()
                    }
                    adapter.notifyDataSetChanged()
                }
                is MyResponse.Error -> Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                else -> {}
            }
        })
    }

    private fun deleteProduct(id: Int) {
        viewModel.deleteProductProvider(id).observe(this, { response ->
            binding.loading.isVisible = response is MyResponse.Loading
            when(response) {
                is MyResponse.Success -> {
                    response.data?.meta?.let {
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                        setupProducts()
                    }
                }
                is MyResponse.Error -> Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                else -> {}
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllProductsProvider()
    }
}