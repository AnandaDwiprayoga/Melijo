package com.pasukanlangit.id.melijo.presentation.mainprovider.home.promo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.razir.progressbutton.hideProgress
import com.pasukanlangit.id.melijo.R
import com.pasukanlangit.id.melijo.data.network.model.request.PromoRequest
import com.pasukanlangit.id.melijo.data.network.model.response.PromoResultItem
import com.pasukanlangit.id.melijo.databinding.ActivityAddUpdatePromoProviderBinding
import com.pasukanlangit.id.melijo.utils.MyResponse
import com.pasukanlangit.id.melijo.utils.MyUtils
import com.pasukanlangit.id.melijo.utils.showLoading
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddUpdatePromoProviderActivity : AppCompatActivity(R.layout.activity_add_update_promo_provider) {

    companion object {
        const val UPDATE_KEY = "update_key"
    }

    private val binding: ActivityAddUpdatePromoProviderBinding by viewBinding()
    private val viewModel: PromoProviderViewModel by viewModels()
    private var promoItem: PromoResultItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MyUtils.setToolbarGreen(window, this)
        promoItem = intent.getParcelableExtra(UPDATE_KEY)
        with(binding) {
            if (promoItem != null) {
                textTitle.text = getString(R.string.title_update_promo)
                edtNamePromo.setText(promoItem?.name)
                edtDescPromo.setText(promoItem?.description)
                edtDiscountPromo.setText(promoItem?.discount.toString())
            }
            buttonCancel.setOnClickListener { finish() }
            backToPromos.setOnClickListener { finish() }
            buttonSave.setOnClickListener { createOrUpdatePromoProcess() }
        }
    }

    private fun createOrUpdatePromoProcess() {
        with(binding) {
            val name = edtNamePromo.text.toString().trim()
            val description = edtDescPromo.text.toString().trim()
            val discount = edtDiscountPromo.text.toString()
            when {
                name.isEmpty() -> {
                    Toast.makeText(this@AddUpdatePromoProviderActivity, "Nama promo tidak boleh kosong", Toast.LENGTH_SHORT).show()
                }
                description.isEmpty() -> {
                    Toast.makeText(this@AddUpdatePromoProviderActivity, "Ketentuan promo tidak boleh kosong", Toast.LENGTH_SHORT).show()
                }
                discount.isEmpty() -> {
                    Toast.makeText(this@AddUpdatePromoProviderActivity, "Nominal diskon tidak boleh kosong", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val mPromoRequest = PromoRequest(name, description, discount.toInt())
                    createOrUpdatePromo(mPromoRequest)
                }
            }
        }
    }

    private fun createOrUpdatePromo(mPromoRequest: PromoRequest) {
        if (promoItem != null) {
            promoItem?.id?.let { promoId ->
                viewModel.updatePromoProvider(promoId, mPromoRequest).observe(this, { response ->
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
        } else {
            viewModel.createPromoProvider(mPromoRequest).observe(this, { response ->
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
}