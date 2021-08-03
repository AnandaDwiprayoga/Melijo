package com.pasukanlangit.id.melijo.presentation.mainprovider.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pasukanlangit.id.melijo.R
import com.pasukanlangit.id.melijo.databinding.ActivityManageProducerBinding
import com.pasukanlangit.id.melijo.presentation.mainprovider.home.category.CategoryProviderActivity
import com.pasukanlangit.id.melijo.presentation.mainprovider.home.product.ProductProviderActivity
import com.pasukanlangit.id.melijo.presentation.mainprovider.home.promo.PromoProviderActivity
import com.pasukanlangit.id.melijo.utils.MyUtils

class ManageProducerActivity : AppCompatActivity(R.layout.activity_manage_producer) {

    private val binding: ActivityManageProducerBinding by viewBinding()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MyUtils.setToolbarGreen(window,this)

        binding.btnBack.setOnClickListener { finish() }
        binding.btnProduct.setOnClickListener {
            startActivity(Intent(this, ProductProviderActivity::class.java))
        }
        binding.btnKategori.setOnClickListener {
            startActivity(Intent(this, CategoryProviderActivity::class.java))
        }
        binding.btnPromo.setOnClickListener {
            startActivity(Intent(this, PromoProviderActivity::class.java))
        }
    }
}