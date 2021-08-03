package com.pasukanlangit.id.melijo.presentation.mainprovider.acount

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.github.razir.progressbutton.hideProgress
import com.pasukanlangit.id.melijo.R
import com.pasukanlangit.id.melijo.data.network.model.response.LoginProducerData
import com.pasukanlangit.id.melijo.data.network.model.response.MetaResponse
import com.pasukanlangit.id.melijo.data.network.model.response.ProfilProducerResponse
import com.pasukanlangit.id.melijo.databinding.FragmentAccountBinding
import com.pasukanlangit.id.melijo.presentation.AuthActivity
import com.pasukanlangit.id.melijo.presentation.mainprovider.MainProviderViewModel
import com.pasukanlangit.id.melijo.presentation.mainprovider.acount.update.ChangeProfileProviderActivity
import com.pasukanlangit.id.melijo.utils.MyResponse
import com.pasukanlangit.id.melijo.utils.setUpToProgressButton
import com.pasukanlangit.id.melijo.utils.showLoading
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountProviderFragment : Fragment(R.layout.fragment_account) {
    private var dataProvider: LoginProducerData ?= null
    private val binding: FragmentAccountBinding by viewBinding()
    private val viewModel: MainProviderViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnChangeProfile.setOnClickListener {
            if(dataProvider == null){
                Toast.makeText(requireContext(),"Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }else{
                Intent(requireContext(), ChangeProfileProviderActivity::class.java).apply {
                    putExtra(ChangeProfileProviderActivity.KEY_PROFILE_DATA, dataProvider)
                    startActivity(this)
                }
            }
        }

        with(binding){
            btnLogout.setUpToProgressButton(this@AccountProviderFragment)
            btnLogout.setOnClickListener {
                viewModel.logout().observe(viewLifecycleOwner){
                    observeLogout(it)
                }
            }
        }
        observeAccountInformation()
    }

    private fun observeLogout(it: MyResponse<MetaResponse>?) {
        when (it) {
            is MyResponse.Success -> {
                binding.btnLogout.hideProgress(getString(R.string.logout))
                it.data?.let { _ ->
                    goToLogin()
                }
            }
            is MyResponse.Loading -> {
                binding.btnLogout.showLoading()
            }
            is MyResponse.Error -> {
                binding.btnLogout.hideProgress(getString(R.string.logout))
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun goToLogin() {
        viewModel.removeAccessToken()
        viewModel.setSession(false)
        activity?.finishAffinity()
        startActivity(Intent(requireContext(), AuthActivity::class.java))
    }

    private fun observeAccountInformation() {
        viewModel.profileProducer.observe(viewLifecycleOwner){
            binding.loading.isVisible = it is MyResponse.Loading
            when (it) {
                is MyResponse.Success -> {
                    it.data?.let { data ->
                        dataProvider = data.result
                        updateUIAccountInformation(data)
                    }
                }
                is MyResponse.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }


    override fun onResume() {
        super.onResume()
        viewModel.getProfileProducer()
    }

    private fun updateUIAccountInformation(data: ProfilProducerResponse) {
        with(binding){
            val dataAccount = data.result
            Glide.with(requireContext())
                .load(dataAccount.photo)
                .circleCrop()
                .into(ivProfile)

            tvNameProfile.text = dataAccount.name
            tvPhoneNumber.text = dataAccount.phoneNumber
            tvEmail.isVisible = false
            labelEmail.isVisible = false
            tvAddress.text = dataAccount.address
        }
    }
}