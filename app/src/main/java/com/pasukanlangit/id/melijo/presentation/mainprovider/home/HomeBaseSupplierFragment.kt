package com.pasukanlangit.id.melijo.presentation.mainprovider.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.pasukanlangit.id.melijo.R
import com.pasukanlangit.id.melijo.databinding.FragmentHomeBaseSupplierBinding
import com.pasukanlangit.id.melijo.presentation.mainprovider.MainProviderViewModel
import com.pasukanlangit.id.melijo.presentation.mainprovider.home.homebasesup.HomeBaseSupPagerAdapter
import com.pasukanlangit.id.melijo.presentation.mainprovider.home.homebasesup.HomeBaseSupType
import com.pasukanlangit.id.melijo.utils.MyResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeBaseSupplierFragment : Fragment(R.layout.fragment_home_base_supplier) {

    private val binding: FragmentHomeBaseSupplierBinding by viewBinding()
    private val viewModel: MainProviderViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAtur.setOnClickListener {
            startActivity(Intent(requireContext(), ManageProducerActivity::class.java))
        }

        setPagerAdapter()
        setUpTabLayout()

        observeProfileProducer()
    }

    private fun observeProfileProducer() {
        viewModel.profileProducer.observe(viewLifecycleOwner){
            binding.loading.isVisible = it is MyResponse.Loading
            when(it){
                is MyResponse.Success -> {
                    with(binding){
                        val profile = it.data?.result
                        Glide.with(requireContext())
                            .load(profile?.photo)
                            .circleCrop()
                            .into(ivSupplier)

                        tvNameSupplier.text = profile?.name
                        tvRating.text = profile?.rating.toString()
                    }
                }
                is MyResponse.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }

    private fun setUpTabLayout() {
        TabLayoutMediator(binding.tabMenuSupplier, binding.pagerSup){
            tab, position -> tab.text = HomeBaseSupType.values()[position].title
        }.attach()
    }

    private fun setPagerAdapter() {
        with(binding.pagerSup){
            adapter = HomeBaseSupPagerAdapter(childFragmentManager, lifecycle)
            isSaveEnabled = false
        }
    }
}