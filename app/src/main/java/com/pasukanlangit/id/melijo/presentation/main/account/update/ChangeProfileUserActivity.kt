package com.pasukanlangit.id.melijo.presentation.main.account.update

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.github.razir.progressbutton.hideProgress
import com.pasukanlangit.id.melijo.R
import com.pasukanlangit.id.melijo.data.network.model.response.ProfileResult
import com.pasukanlangit.id.melijo.databinding.ActivityChangeProfileUserBinding
import com.pasukanlangit.id.melijo.utils.*
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody


@AndroidEntryPoint
class ChangeProfileUserActivity : AppCompatActivity(R.layout.activity_change_profile_user) {

    private val PERMISSIONS = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private var imageUri: Uri? = null
    private val binding: ActivityChangeProfileUserBinding by viewBinding()
    private val viewModel: ChangeProfileUserViewModel by viewModels()

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

        intent.getParcelableExtra<ProfileResult>(KEY_PROFILE_DATA)?.let { data -> setUpUI(data) }

        with(binding) {
            btnEdit.setUpToProgressButton(this@ChangeProfileUserActivity)

            btnChangeProfile.setOnClickListener {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    selectImageIntent()
                    return@setOnClickListener
                }

                if (hasPermissions(PERMISSIONS)) {
                    selectImageIntent()
                } else {
                    permReqLauncher.launch(
                        PERMISSIONS
                    )
                }
            }
            btnEdit.setOnClickListener { btnEditOnClick() }
        }

        observeAccountUpdated()
    }

    private fun hasPermissions(permissions: Array<String>): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun observeAccountUpdated() {
        viewModel.updatedProfile.observe(this) {
            when (it) {
                is MyResponse.Success -> {
                    binding.btnEdit.hideProgress(getString(R.string.ubah_profile))
                    Toast.makeText(this, "Berhasil diupdate", Toast.LENGTH_SHORT).show()

                    with(Intent()) {
                        putExtra(KEY_IS_UPDATED_PROFILE, true)
                        setResult(RESULT_OK, this)
                        finish()
                    }
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
            val email = edtEmail.text.toString().trim()
            val phoneNumber = edtPhonenumber.text.toString().trim()
            val address = edtAddress.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || address.isEmpty()) {
                Toast.makeText(this@ChangeProfileUserActivity, "Input empty", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val nameRequestBody = name.toRequestBody(MultipartBody.FORM)
                val emailRequestBody = email.toRequestBody(MultipartBody.FORM)
                val phoneRequestBody = phoneNumber.toRequestBody(MultipartBody.FORM)
                val addressRequestBody = address.toRequestBody(MultipartBody.FORM)
                val imageBody: MultipartBody.Part? =
                    if (imageUri == null) null else UploadUtil.getImageMultipart(
                        this@ChangeProfileUserActivity,
                        imageUri,
                        "photo"
                    )

                viewModel.updateProfileUser(
                    nameRequestBody,
                    emailRequestBody,
                    addressRequestBody,
                    phoneRequestBody,
                    imageBody
                )
            }
        }
    }

    private fun setUpUI(data: ProfileResult) {
        with(binding) {
            Glide.with(this@ChangeProfileUserActivity)
                .load(data.photo)
                .centerCrop()
                .into(ivProfile)

            edtName.setText(data.name)
            edtEmail.setText(data.email)
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
        const val KEY_IS_UPDATED_PROFILE = "KEY_PROFILE_DATA"
    }
}