package com.pasukanlangit.id.melijo.presentation.mainprovider.home.promo

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
import com.pasukanlangit.id.melijo.databinding.ActivityPromoProviderBinding
import com.pasukanlangit.id.melijo.utils.MyResponse
import com.pasukanlangit.id.melijo.utils.MyUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PromoProviderActivity : AppCompatActivity(R.layout.activity_promo_provider) {

    private val binding: ActivityPromoProviderBinding by viewBinding()
    private val viewModel: PromoProviderViewModel by viewModels()
    private lateinit var mPromoProviderAdapter: PromoProviderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MyUtils.setToolbarGreen(window, this)
        mPromoProviderAdapter = PromoProviderAdapter(this)
        setupPromos()
        binding.buttonAddPromo.setOnClickListener {
            startActivity(Intent(this, AddUpdatePromoProviderActivity::class.java))
        }
        binding.btnBack.setOnClickListener { finish() }
        mPromoProviderAdapter.setOnClickDeleteButtonListener(object: PromoProviderAdapter.OnDeleteButtonClickListener {
            override fun onDeleteButtonClicked(promoId: Int) {
                val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this@PromoProviderActivity)
                alertDialog.setCancelable(false)
                alertDialog.setTitle("Konfirmasi Hapus Promo")
                alertDialog.setMessage("Apakah anda yakin menghapus promo ini?")
                alertDialog.setPositiveButton("Yakin") { dialog: DialogInterface, _ ->
                    deletePromo(promoId)
                    dialog.dismiss()
                }
                alertDialog.setNegativeButton("Batal") { dialog: DialogInterface, _ ->
                    dialog.dismiss()
                }
                alertDialog.show()
            }
        })
    }

    private fun setupPromos() {
        with(binding.rvPromo) {
            layoutManager = LinearLayoutManager(this@PromoProviderActivity)
            adapter = mPromoProviderAdapter
            setHasFixedSize(true)
        }

        viewModel.promoProvider.observe(this, { response ->
            binding.loading.isVisible = response is MyResponse.Loading
            when(response) {
                is MyResponse.Success -> {
                    response.data?.result?.let { data ->
                        mPromoProviderAdapter.setPromoProvider(data)
                        mPromoProviderAdapter.notifyDataSetChanged()
                    }
                    mPromoProviderAdapter.notifyDataSetChanged()
                }
                is MyResponse.Error -> Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                else -> {}
            }
        })
    }

    private fun deletePromo(promoId: Int) {
        viewModel.deletePromoProvider(promoId).observe(this, { response ->
            binding.loading.isVisible = response is MyResponse.Loading
            when(response) {
                is MyResponse.Success -> {
                    response.data?.meta?.let {
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                        viewModel.collectPromoProvider()
                    }
                }
                is MyResponse.Error -> Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                else -> {}
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.collectPromoProvider()
    }
}