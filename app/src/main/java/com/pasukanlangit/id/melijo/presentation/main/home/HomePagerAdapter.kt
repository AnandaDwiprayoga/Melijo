package com.pasukanlangit.id.melijo.presentation.main.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pasukanlangit.id.melijo.presentation.main.home.seller.SellerHomeFragment
import com.pasukanlangit.id.melijo.presentation.main.home.supplier.SupplierHomeFragment

class HomePagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {
    companion object {
        val TITLES = arrayOf("Pasar","Belanja")
    }
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> SupplierHomeFragment()
            1 -> SellerHomeFragment()
            else -> Fragment()
        }
}