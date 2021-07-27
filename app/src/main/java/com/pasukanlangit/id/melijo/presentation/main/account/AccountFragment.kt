package com.pasukanlangit.id.melijo.presentation.main.account

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.github.razir.progressbutton.hideProgress
import com.pasukanlangit.id.melijo.R
import com.pasukanlangit.id.melijo.data.network.model.response.MetaResponse
import com.pasukanlangit.id.melijo.data.network.model.response.ProfileResult
import com.pasukanlangit.id.melijo.data.network.model.response.UserProfileResponse
import com.pasukanlangit.id.melijo.databinding.FragmentAccountBinding
import com.pasukanlangit.id.melijo.presentation.AuthActivity
import com.pasukanlangit.id.melijo.presentation.auth.UserType
import com.pasukanlangit.id.melijo.presentation.main.MainViewModel
import com.pasukanlangit.id.melijo.presentation.main.account.update.ChangeProfileUserActivity
import com.pasukanlangit.id.melijo.utils.MyResponse
import com.pasukanlangit.id.melijo.utils.setUpToProgressButton
import com.pasukanlangit.id.melijo.utils.showLoading
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountFragment : Fragment(R.layout.fragment_account) {
    private var dataUser: ProfileResult ?= null
    private val binding: FragmentAccountBinding by viewBinding()
    private val viewModel: MainViewModel by viewModels()
    private var isMustUpdateSharedPref :Boolean ?= false

    private var accessToken : String ?= null

    private var myActivityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == AppCompatActivity.RESULT_OK) {
            isMustUpdateSharedPref = it.data?.getBooleanExtra(ChangeProfileUserActivity.KEY_IS_UPDATED_PROFILE, false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        accessToken = viewModel.getAccessToken()
        val accountType = viewModel.getAccountType()

        with(binding){
            btnChangeProfile.setOnClickListener {
                if(dataUser == null){
                    Toast.makeText(requireContext(),"Gagal mengambil data user", Toast.LENGTH_SHORT).show()
                }else{
                    Intent(requireContext(), ChangeProfileUserActivity::class.java).apply {
                        putExtra(ChangeProfileUserActivity.KEY_PROFILE_DATA, dataUser)
                        myActivityResultLauncher.launch(this)
                    }
                }
            }

            btnLogout.setUpToProgressButton(this@AccountFragment)
            btnLogout.setOnClickListener {
                accessToken?.let { token ->
                    if(accountType.isNullOrEmpty() || accessToken.isNullOrEmpty()){
                        Toast.makeText(requireContext(), "Logout Faield", Toast.LENGTH_SHORT).show()
                    }else{
                        if(accountType == UserType.TYPE_BUYER.value){
                            viewModel.logout(token, UserType.TYPE_BUYER).observe(viewLifecycleOwner){
                                updateUILogout(it)
                            }
                        }else{
                            viewModel.logout(token, UserType.TYPE_SELLER).observe(viewLifecycleOwner){
                                updateUILogout(it)
                            }
                        }
                    }
                }
            }
        }

    }

    private fun observeAccountInformation() {
        accessToken?.let { token ->
            viewModel.getUserProfile(token).observe(viewLifecycleOwner){
                binding.loading.isVisible = it is MyResponse.Loading
                when (it) {
                    is MyResponse.Success -> {
                        it.data?.let { data ->
                           dataUser = data.result
                           if(isMustUpdateSharedPref == true){
                               viewModel.setImageUser(data.result.photo)
                               viewModel.setNameUser(data.result.name)
                           }
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
    }

    private fun updateUIAccountInformation(data: UserProfileResponse) {
        with(binding){
            val dataAccount = data.result
            Glide.with(this@AccountFragment)
                .load(dataAccount.photo)
                .circleCrop()
                .into(ivProfile)

            tvNameProfile.text = dataAccount.name
            tvPhoneNumber.text = dataAccount.phoneNumber
            tvEmail.text = dataAccount.email
            tvAddress.text = dataAccount.address
        }
    }

    private fun updateUILogout(it: MyResponse<out MetaResponse?>?) {
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

    override fun onResume() {
        super.onResume()
        observeAccountInformation()
    }

    private fun goToLogin() {
        viewModel.removeAccessToken()
        viewModel.setSession(false)
        activity?.finishAffinity()
        startActivity(Intent(requireContext(), AuthActivity::class.java))
    }
}