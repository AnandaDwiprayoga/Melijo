package com.pasukanlangit.id.melijo.presentation.mainprovider.home.product

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.razir.progressbutton.hideProgress
import com.pasukanlangit.id.melijo.R
import com.pasukanlangit.id.melijo.data.network.model.request.ProductRequest
import com.pasukanlangit.id.melijo.databinding.ActivityAddUpdateProductBinding
import com.pasukanlangit.id.melijo.presentation.mainprovider.home.category.CategoryProviderViewModel
import com.pasukanlangit.id.melijo.utils.MyResponse
import com.pasukanlangit.id.melijo.utils.MyUtils
import com.pasukanlangit.id.melijo.utils.showLoading
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddUpdateProductActivity : AppCompatActivity(R.layout.activity_add_update_product) {

    private val binding: ActivityAddUpdateProductBinding by viewBinding()
    private val viewModelCategory: CategoryProviderViewModel by viewModels()
    private val viewModel: ProductProviderViewModel by viewModels()
    private var categoryId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MyUtils.setToolbarGreen(window, this)
        setupCategory()

        with(binding) {
            buttonCancel.setOnClickListener { finish() }
            backToProducts.setOnClickListener { finish() }
            buttonSave.setOnClickListener { addOrUpdateProductProcess() }
            edtDiscountProduct.setText(getString(R.string.zero_value))
        }
    }

    private fun setupCategory() {
        val categories: ArrayList<String> = arrayListOf()
        val categoriesId: ArrayList<Int> = arrayListOf()
        viewModelCategory.categoryProvider.observe(this, { response ->
            when(response) {
                is MyResponse.Success -> {
                    response.data?.result?.forEach { result ->
                        categories.add(result.name)
                        categoriesId.add(result.id)
                    }
                    categories.add(0, getString(R.string.choose_category))
                    categoriesId.add(0, -1)
                    binding.chooseCategory.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)
                }
                is MyResponse.Error -> Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                else -> {}
            }
        })
        binding.chooseCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (parent.getItemAtPosition(position).equals(getString(R.string.choose_category))) {
                    return
                } else {
                    categoryId = categoriesId[position]
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(this@AddUpdateProductActivity, "Silahkan pilih kategori barang!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addOrUpdateProductProcess() {
        with(binding) {
            when {
                categoryId == -1 -> Toast.makeText(this@AddUpdateProductActivity, "Silahkan pilih kategori barang", Toast.LENGTH_SHORT).show()
                edtNameProduct.text.isEmpty() -> Toast.makeText(this@AddUpdateProductActivity, "Nama barang belum diisi!", Toast.LENGTH_SHORT).show()
                edtPriceProduct.text.isEmpty() -> Toast.makeText(this@AddUpdateProductActivity, "Harga barang belum diisi!", Toast.LENGTH_SHORT).show()
                edtStockProduct.text.isEmpty() -> Toast.makeText(this@AddUpdateProductActivity, "Stok barang belum diisi!", Toast.LENGTH_SHORT).show()
                else -> {
                    val mProductRequest = if (edtDescriptionProduct.text.toString().isEmpty()) {
                            ProductRequest(
                                categoryId,
                                edtNameProduct.text.toString().trim(),
                                edtPriceProduct.text.toString().toInt(),
                                edtStockProduct.text.toString().toInt(),
                                edtDiscountProduct.text.toString().toInt(),
                            )
                        } else {
                            ProductRequest(
                                categoryId,
                                edtNameProduct.text.toString().trim(),
                                edtPriceProduct.text.toString().toInt(),
                                edtStockProduct.text.toString().toInt(),
                                edtDiscountProduct.text.toString().toInt(),
                                edtDescriptionProduct.text.toString().trim()
                            )
                        }
                    addOrUpdateProduct(mProductRequest)
                }
            }
        }
    }

    private fun addOrUpdateProduct(mProductProvider: ProductRequest) {
        viewModel.createProductProvider(mProductProvider).observe(this, { response ->
            when(response) {
                is MyResponse.Loading -> binding.buttonSave.showLoading()
                is MyResponse.Success -> {
                    binding.buttonSave.hideProgress(getString(R.string.save))
                    response.data?.meta?.let {
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
                is MyResponse.Error -> {
                    binding.buttonSave.hideProgress(getString(R.string.save))
                    Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}