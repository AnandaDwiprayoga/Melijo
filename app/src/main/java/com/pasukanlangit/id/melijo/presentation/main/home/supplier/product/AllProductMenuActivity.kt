package com.pasukanlangit.id.melijo.presentation.main.home.supplier.product

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.pasukanlangit.id.melijo.R
import com.pasukanlangit.id.melijo.databinding.ActivityAllProductMenuBinding
import com.pasukanlangit.id.melijo.presentation.main.home.HomePagerAdapter
import com.pasukanlangit.id.melijo.utils.MyUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllProductMenuActivity : AppCompatActivity(R.layout.activity_all_product_menu) {

    private val binding: ActivityAllProductMenuBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MyUtils.setToolbarGreen(window, this)
        binding.btnBack.setOnClickListener { finish() }

        setUpViewPager()
        setUpTabMenu()
    }

    private fun setUpViewPager() {
        with(binding.viewpager){
            adapter = SupplierPagerAdapter(supportFragmentManager,lifecycle)
            isSaveEnabled = false
        }
    }

    private fun setUpTabMenu() {
        TabLayoutMediator(binding.tabMenu, binding.viewpager){
                tab, position -> tab.text = SupplierPagerAdapter.TITLES[position]
        }.attach()
    }
}