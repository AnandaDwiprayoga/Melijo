package com.pasukanlangit.id.melijo.presentation.mainprovider.acount.update

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.github.razir.progressbutton.hideProgress
import com.pasukanlangit.id.melijo.R
import com.pasukanlangit.id.melijo.data.network.model.response.LoginProducerData
import com.pasukanlangit.id.melijo.databinding.ActivityChangeProfileUserBinding
import com.pasukanlangit.id.melijo.utils.*
import com.pasukanlangit.id.melijo.utils.MyUtils.hasPermissions
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody


@AndroidEntryPoint
class ChangeProfileProviderActivity : AppCompatActivity(R.layout.activity_change_profile_user) {

    private var imageUri: Uri? = null
    private val binding: ActivityChangeProfileUserBinding by viewBinding()
    private val viewModel: ChangeProfileProviderViewModel by viewModels()

    private var myActivityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            StartActivityForResult()
        ) {
            if (it.resultCode == RESULT_OK) {
                imageUri = it.data?.data
                Glide.with(this)
                    .load(imageUri)
                    .centerCrop()
                    .into(binding.ivProfile)
            }
        }

    private val permReqLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all {
                it.value == true
            }
            if (granted) {
                selectImageIntent()
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MyUtils.setToolbarGreen(window, this)

        intent.getParcelableExtra<LoginProducerData>(KEY_PROFILE_DATA)?.let { data -> setUpUI(data) }

        with(binding) {
            btnBack.setOnClickListener { finish() }
            btnEdit.setUpToProgressButton(this@ChangeProfileProviderActivity)

            btnChangeProfile.setOnClickListener {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    selectImageIntent()
                    return@setOnClickListener
                }

                if (hasPermissions(this@ChangeProfileProviderActivity, PERMISSIONS_STORAGE)) {
                    selectImageIntent()
                } else {
                    permReqLauncher.launch(
                        PERMISSIONS_STORAGE
                    )
                }
            }
            btnEdit.setOnClickListener { btnEditOnClick() }
        }

        observeAccountUpdated()
    }



    private fun observeAccountUpdated() {
        viewModel.updatedProfile.observe(this) {
            when (it) {
                is MyResponse.Success -> {
                    binding.btnEdit.hideProgress(getString(R.string.ubah_profile))
                    Toast.makeText(this, "Berhasil diupdate", Toast.LENGTH_SHORT).show()

                    finish()
                }
                is MyResponse.Loading -> {
                    binding.btnEdit.showLoading()
                }
                is MyResponse.Error -> {
                    binding.btnEdit.hideProgress(getString(R.string.ubah_profile))
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun btnEditOnClick() {
        with(binding) {
            val name = edtName.text.toString().trim()
            val phoneNumber = edtPhonenumber.text.toString().trim()
            val address = edtAddress.text.toString().trim()

            if (name.isEmpty()  || phoneNumber.isEmpty() || address.isEmpty()) {
                Toast.makeText(this@ChangeProfileProviderActivity, "Input empty", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val nameRequestBody = name.toRequestBody(MultipartBody.FORM)
                val phoneRequestBody = phoneNumber.toRequestBody(MultipartBody.FORM)
                val addressRequestBody = address.toRequestBody(MultipartBody.FORM)
                val imageBody: MultipartBody.Part? =
                    if (imageUri == null) null else UploadUtil.getImageMultipart(
                        this@ChangeProfileProviderActivity,
                        imageUri,
                        "photo"
                    )

                viewModel.updateProfileProvider(
                    nameRequestBody,
                    addressRequestBody,
                    phoneRequestBody,
                    imageBody
                )
            }
        }
    }

    private fun setUpUI(data: LoginProducerData) {
        with(binding) {
            Glide.with(this@ChangeProfileProviderActivity)
                .load(data.photo)
                .centerCrop()
                .into(ivProfile)

            edtName.setText(data.name)
            edtEmail.isVisible = false
//            edtEmail.setText(data.email)
            edtPhonenumber.setText(data.phoneNumber)
            edtAddress.setText(data.address)
        }
    }

    private fun selectImageIntent() {
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        myActivityResultLauncher.launch(photoPickerIntent)
    }

    companion object {
        const val KEY_PROFILE_DATA = "KEY_PROFILE_DATA"
    }
}