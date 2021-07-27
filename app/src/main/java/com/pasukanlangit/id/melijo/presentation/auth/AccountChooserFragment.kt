package com.pasukanlangit.id.melijo.presentation.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pasukanlangit.id.melijo.R
import com.pasukanlangit.id.melijo.databinding.FragmentAccountChooserBinding

class AccountChooserFragment : Fragment(R.layout.fragment_account_chooser) {
    private val binding: FragmentAccountChooserBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBuyer.setOnClickListener {
            val action = AccountChooserFragmentDirections.actionAccountChooserFragmentToLoginFragment(userType = UserType.TYPE_BUYER)
            findNavController().navigate(action)
        }
        
        binding.btnSeller.setOnClickListener {
            val action = AccountChooserFragmentDirections.actionAccountChooserFragmentToLoginFragment(userType = UserType.TYPE_SELLER)
            findNavController().navigate(action)
        }
        binding.btnSupplier.setOnClickListener {
            val action = AccountChooserFragmentDirections.actionAccountChooserFragmentToLoginFragment(userType = UserType.TYPE_SUPPLIER)
            findNavController().navigate(action)
        }
    }
}