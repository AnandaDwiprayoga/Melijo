package com.pasukanlangit.id.melijo.presentation.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.razir.progressbutton.hideProgress
import com.pasukanlangit.id.melijo.R
import com.pasukanlangit.id.melijo.data.network.model.request.RegisterRequest
import com.pasukanlangit.id.melijo.databinding.FragmentRegisterBinding
import com.pasukanlangit.id.melijo.presentation.AuthViewModel
import com.pasukanlangit.id.melijo.utils.MyResponse
import com.pasukanlangit.id.melijo.utils.setUpToProgressButton
import com.pasukanlangit.id.melijo.utils.showLoading
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {
    private val binding : FragmentRegisterBinding by viewBinding()
    private val args: RegisterFragmentArgs by navArgs()
    private val viewModel: AuthViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnBackPress()

        with(binding){
            edtEmail.isVisible = args.userType == UserType.TYPE_BUYER
            btnRegistration.setUpToProgressButton(this@RegisterFragment)
            btnRegistration.setOnClickListener {
                registerAccount()
            }
        }
    }

    private fun registerAccount() {
        with(binding){
            val username = edtUsername.text.toString().trim()
            val email = edtEmail.text.toString().trim()
            val name = edtName.text.toString().trim()
            val noHp = edtPhonenumber.text.toString().trim()
            val address = edtAddress.text.toString().trim()
            val password = edtPassword.text.toString().trim()
            val passwordConfirmation = edtPasswordConfirmation.text.toString().trim()

            if(username.isEmpty() || (edtEmail.isVisible && email.isEmpty())|| name.isEmpty() || noHp.isEmpty() || address.isEmpty() || password.isEmpty()){
                Toast.makeText(requireContext(), getString(R.string.input_cannot_empty), Toast.LENGTH_SHORT).show()
                return
            }
            if(password != passwordConfirmation){
                Toast.makeText(requireContext(), getString(R.string.password_not_match), Toast.LENGTH_SHORT).show()
                return
            }

            val register = RegisterRequest(username,name,email,password,address,noHp)
            submitRegister(register)
        }
    }

    private fun submitRegister(register: RegisterRequest) {
        viewModel.register(register, args.userType).observe(viewLifecycleOwner){
            when(it){
                is MyResponse.Success -> {
                    binding.btnRegistration.hideProgress(getString(R.string.register))
                    Toast.makeText(requireContext(), "Registrasi Success", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack(R.id.loginFragment, false)
                }
                is MyResponse.Loading -> {
                    binding.btnRegistration.showLoading()
                }
                is MyResponse.Error -> {
                    binding.btnRegistration.hideProgress(getString(R.string.register))
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setOnBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack(R.id.loginFragment, false)
            }
        })
    }
}