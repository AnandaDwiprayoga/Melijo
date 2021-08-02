package com.pasukanlangit.id.melijo.presentation.mainprovider.home.homebasesup

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

enum class HomeBaseSupType(val title: String) {
    TYPE_PRODUCT("Product"),
    TYPE_PROMO("Promo"),
    TYPE_KATEGORI("Kategori")
}
class HomeBaseSupPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = HomeBaseSupType.values().size

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> HomeSupProductFragment(HomeBaseSupType.TYPE_PRODUCT)
            1 -> HomeSupProductFragment(HomeBaseSupType.TYPE_PROMO)
            2 -> HomeSupProductFragment(HomeBaseSupType.TYPE_KATEGORI)
            else -> Fragment()
        }
}