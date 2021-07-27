package com.pasukanlangit.id.melijo.presentation.main.home.supplier

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pasukanlangit.id.melijo.R
import com.pasukanlangit.id.melijo.databinding.FragmentHomeSupplierBinding
import com.pasukanlangit.id.melijo.presentation.main.MainViewModel
import com.pasukanlangit.id.melijo.utils.GridSpacingItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SupplierHomeFragment : Fragment(R.layout.fragment_home_supplier){
    private val binding: FragmentHomeSupplierBinding by viewBinding()

    private val viewModel: MainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            rvMenu.layoutManager = GridLayoutManager(requireContext(),4)
            rvMenu.adapter = MenuAdapter(getDataMenuSupplier())
            rvMenu.addItemDecoration(GridSpacingItemDecoration(8))
        }
    }
}