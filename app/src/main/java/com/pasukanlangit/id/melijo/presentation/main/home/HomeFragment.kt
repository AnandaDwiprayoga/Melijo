package com.pasukanlangit.id.melijo.presentation.main.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.pasukanlangit.id.melijo.R
import com.pasukanlangit.id.melijo.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home){
    private val binding : FragmentHomeBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setPagerAdapter()
        setUpTabLayout()
    }


    fun setUserCanScroll(value: Boolean){
        binding.pagerHome.isUserInputEnabled = value
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun setUpTabLayout() {
        TabLayoutMediator(binding.tabHome, binding.pagerHome){
            tab, position -> tab.text = HomePagerAdapter.TITLES[position]
        }.attach()
    }

    private fun setPagerAdapter() {
        with(binding){
            pagerHome.adapter = HomePagerAdapter(childFragmentManager, lifecycle)
            pagerHome.isSaveEnabled = false
        }
    }
}