package com.pasukanlangit.id.melijo.presentation.mainprovider.home.product

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.activity.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.github.razir.progressbutton.hideProgress
import com.pasukanlangit.id.melijo.R
import com.pasukanlangit.id.melijo.data.network.model.response.ProductItem
import com.pasukanlangit.id.melijo.databinding.ActivityAddUpdateProductBinding
import com.pasukanlangit.id.melijo.presentation.mainprovider.home.category.CategoryProviderViewModel
import com.pasukanlangit.id.melijo.utils.MyResponse
import com.pasukanlangit.id.melijo.utils.MyUtils
import com.pasukanlangit.id.melijo.utils.MyUtils.hasPermissions
import com.pasukanlangit.id.melijo.utils.UploadUtil
import com.pasukanlangit.id.melijo.utils.showLoading
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

@AndroidEntryPoint
class AddUpdateProductActivity : AppCompatActivity(R.layout.activity_add_update_product) {

    companion object {
        const val KEY_UPDATE_PRODUCT = "key_update_product"
    }

    private val PERMISSIONS = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    private var imageUri: Uri? = null
    private var activityForResult = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            imageUri = result.data?.data
            Glide.with(this)
                .load(imageUri)
                .centerCrop()
                .into(binding.imgProduct)
        }
    }

    private val permissionRequestLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all {
                it.value == true
            }
            if (granted) {
                selectImageIntent()
            }
        }

    private val binding: ActivityAddUpdateProductBinding by viewBinding()
    private val viewModelCategory: CategoryProviderViewModel by viewModels()
    private val viewModel: ProductProviderViewModel by viewModels()
    private var itemProduct: ProductItem? = null
    private var categoryId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MyUtils.setToolbarGreen(window, this)
        itemProduct = intent.getParcelableExtra(KEY_UPDATE_PRODUCT)
        setupCategory()

        with(binding) {
            edtDiscountProduct.setText(getString(R.string.zero_value))
            if (itemProduct != null) {
                edtNameProduct.setText(itemProduct?.name)
                edtPriceProduct.setText(itemProduct?.price.toString())
                edtStockProduct.setText(itemProduct?.stock.toString())
                edtDiscountProduct.setText(itemProduct?.promo?.toString() ?: "")
                edtDescriptionProduct.setText(itemProduct?.description)
                Glide.with(this@AddUpdateProductActivity)
                    .load(itemProduct?.picture)
                    .centerCrop()
                    .into(imgProduct)
                textTitle.text = getText(R.string.title_update_product)
            }
            buttonCancel.setOnClickListener { finish() }
            backToProducts.setOnClickListener { finish() }
            buttonSave.setOnClickListener { addOrUpdateProductProcess() }
            buttonChooseImage.setOnClickListener {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    selectImageIntent()
                    return@setOnClickListener
                }

                if (hasPermissions(this@AddUpdateProductActivity, PERMISSIONS)) {
                    selectImageIntent()
                } else {
                    permissionRequestLauncher.launch(PERMISSIONS)
                }
            }
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
                    val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)
                    binding.chooseCategory.adapter = arrayAdapter
                    if (itemProduct != null) {
                        for (i in 0 until categoriesId.size) {
                            if (categoriesId[i] == itemProduct?.categoryId) {
                                binding.chooseCategory.setSelection(i)
                                categoryId = categoriesId[i]
                            }
                        }
                    }
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

    private fun selectImageIntent() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        activityForResult.launch(intent)
    }

    private fun addOrUpdateProductProcess() {
        with(binding) {
            when {
                categoryId == -1 -> Toast.makeText(this@AddUpdateProductActivity, "Silahkan pilih kategori barang", Toast.LENGTH_SHORT).show()
                edtNameProduct.text.isEmpty() -> Toast.makeText(this@AddUpdateProductActivity, "Nama barang belum diisi!", Toast.LENGTH_SHORT).show()
                edtPriceProduct.text.isEmpty() -> Toast.makeText(this@AddUpdateProductActivity, "Harga barang belum diisi!", Toast.LENGTH_SHORT).show()
                edtStockProduct.text.isEmpty() -> Toast.makeText(this@AddUpdateProductActivity, "Stok barang belum diisi!", Toast.LENGTH_SHORT).show()
                else -> {
                    val categoryRequest = categoryId.toString().toRequestBody(MultipartBody.FORM)
                    val nameRequest = edtNameProduct.text.toString().trim().toRequestBody(MultipartBody.FORM)
                    val stockRequest = edtStockProduct.text.toString().toRequestBody(MultipartBody.FORM)
                    val priceRequest = edtPriceProduct.text.toString().toRequestBody(MultipartBody.FORM)
                    val promoRequest = edtDiscountProduct.text.toString().toRequestBody(MultipartBody.FORM)
                    val descriptionRequest = if (edtDescriptionProduct.text.toString().trim().isEmpty()) null else edtDescriptionProduct.text.toString().toRequestBody(MultipartBody.FORM)
                    val pictureRequest: MultipartBody.Part? = if (imageUri == null) null else UploadUtil.getImageMultipart(this@AddUpdateProductActivity, imageUri)

                    if (itemProduct != null) {
                        itemProduct?.id?.let { id ->
                            viewModel.updateProductProvider(
                                id,
                                categoryRequest,
                                nameRequest,
                                stockRequest,
                                priceRequest,
                                promoRequest,
                                descriptionRequest,
                                pictureRequest
                            ).observe(this@AddUpdateProductActivity, { response ->
                                when(response) {
                                    is MyResponse.Loading -> buttonSave.showLoading()
                                    is MyResponse.Success -> {
                                        buttonSave.hideProgress(getString(R.string.save))
                                        response.data?.meta?.let {
                                            Toast.makeText(this@AddUpdateProductActivity, it.message, Toast.LENGTH_SHORT).show()
                                        }
                                        finish()
                                    }
                                    is MyResponse.Error -> {
                                        buttonSave.hideProgress(getString(R.string.save))
                                        Toast.makeText(this@AddUpdateProductActivity, response.message, Toast.LENGTH_SHORT).show()
                                    }
                                }
                            })
                        }
                    } else {
                        viewModel.createProductProvider(
                            categoryRequest,
                            nameRequest,
                            stockRequest,
                            priceRequest,
                            promoRequest,
                            descriptionRequest,
                            pictureRequest
                        ).observe(this@AddUpdateProductActivity, { response ->
                            when(response) {
                                is MyResponse.Loading -> buttonSave.showLoading()
                                is MyResponse.Success -> {
                                    buttonSave.hideProgress(getString(R.string.save))
                                    response.data?.meta?.let {
                                        Toast.makeText(this@AddUpdateProductActivity, it.message, Toast.LENGTH_SHORT).show()
                                    }
                                    finish()
                                }
                                is MyResponse.Error -> {
                                    buttonSave.hideProgress(getString(R.string.save))
                                    Toast.makeText(this@AddUpdateProductActivity, response.message, Toast.LENGTH_SHORT).show()
                                }
                            }
                        })
                    }
                }
            }
        }
    }
}