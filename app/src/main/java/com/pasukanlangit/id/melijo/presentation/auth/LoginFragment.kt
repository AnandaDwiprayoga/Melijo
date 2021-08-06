package com.pasukanlangit.id.melijo.presentation.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.razir.progressbutton.hideProgress
import com.pasukanlangit.id.melijo.R
import com.pasukanlangit.id.melijo.data.network.model.request.LoginRequest
import com.pasukanlangit.id.melijo.data.network.model.request.UpdateLocationRequest
import com.pasukanlangit.id.melijo.databinding.FragmentLoginBinding
import com.pasukanlangit.id.melijo.presentation.AuthActivity
import com.pasukanlangit.id.melijo.presentation.AuthViewModel
import com.pasukanlangit.id.melijo.utils.MyResponse
import com.pasukanlangit.id.melijo.utils.setUpToProgressButton
import com.pasukanlangit.id.melijo.utils.showLoading
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private val args: LoginFragmentArgs by navArgs()
    private val binding: FragmentLoginBinding by viewBinding()
    private val viewModel: AuthViewModel by viewModels()
    private var isSaveSession: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnBackPress()

        with(binding) {
            btnLogin.setUpToProgressButton(this@LoginFragment)
            btnLogin.setOnClickListener { loginProcess() }
            tvRegister.setOnClickListener {
                val action =
                    LoginFragmentDirections.actionLoginFragmentToRegisterFragment(userType = args.userType)
                findNavController().navigate(action)
            }
        }

    }

    private fun setOnBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack(R.id.accountChooserFragment, false)
                }
            })
    }


    private fun loginProcess() {
        with(binding) {
            val userName = edtUsername.text.toString().trim()
            val password = edtPassword.text.toString().trim()
            isSaveSession = cbRememberMe.isChecked

            if (userName.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Input tidak boleh kosong", Toast.LENGTH_SHORT)
                    .show()
                return
            }

            val loginRequest = LoginRequest(userName, password)

            if (args.userType == UserType.TYPE_BUYER) {
                loginUser(loginRequest)
            } else {
                loginProducer(loginRequest)
            }
        }
    }

    private fun loginProducer(loginRequest: LoginRequest) {
        viewModel.loginProducer(loginRequest).observe(viewLifecycleOwner) {
            when (it) {
                is MyResponse.Success -> {
                    binding.btnLogin.hideProgress(getString(R.string.login))
                    it.data?.let { data ->
                        viewModel.saveLocationUser(UpdateLocationRequest(data.result.data.latitude.toDouble(), data.result.data.longitude.toDouble()))
                        navigateToMainAndSetToken(data.result.accessToken, data.result.tokenLevel)
                    }
                }
                is MyResponse.Loading -> {
                    binding.btnLogin.showLoading()
                }
                is MyResponse.Error -> {
                    binding.btnLogin.hideProgress(getString(R.string.login))
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun navigateToMainAndSetToken(accessToken: String, accountType: String) {
        viewModel.setSession(isSaveSession)
        viewModel.setAccessToken(accessToken)
        viewModel.setAccountType(accountType)
        (activity as AuthActivity).navigateToMain(accountType)
    }

    private fun loginUser(loginRequest: LoginRequest) {
        viewModel.loginUser(loginRequest).observe(viewLifecycleOwner) {
            when (it) {
                is MyResponse.Success -> {
                    binding.btnLogin.hideProgress(getString(R.string.login))
                    it.data?.let { data ->
                        viewModel.saveImageUser(data.result.data.photo)
                        viewModel.saveNameUser(data.result.data.name)
                        viewModel.saveLocationUser(UpdateLocationRequest(data.result.data.latitude.toDouble(), data.result.data.longitude.toDouble()))
                        navigateToMainAndSetToken(data.result.accessToken, data.result.tokenLevel)
                    }
                }
                is MyResponse.Loading -> {
                    binding.btnLogin.showLoading()
                }
                is MyResponse.Error -> {
                    binding.btnLogin.hideProgress(getString(R.string.login))
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}