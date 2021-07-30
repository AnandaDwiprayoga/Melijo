package com.pasukanlangit.id.melijo.presentation.mainprovider.home.category

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.razir.progressbutton.hideProgress
import com.pasukanlangit.id.melijo.R
import com.pasukanlangit.id.melijo.data.network.model.request.CategoryRequest
import com.pasukanlangit.id.melijo.databinding.ActivityAddUpdateCategoryBinding
import com.pasukanlangit.id.melijo.utils.MyResponse
import com.pasukanlangit.id.melijo.utils.MyUtils
import com.pasukanlangit.id.melijo.utils.showLoading
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddUpdateCategoryActivity : AppCompatActivity(R.layout.activity_add_update_category) {

    private val binding: ActivityAddUpdateCategoryBinding by viewBinding()
    private val viewModel: CategoryProviderViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MyUtils.setToolbarGreen(window, this)

        with(binding) {
            buttonCancel.setOnClickListener{ finish() }
            backToCategory.setOnClickListener { finish() }
            buttonSave.setOnClickListener { createCategoryProcess() }
        }
    }

    private fun createCategoryProcess() {
        with(binding) {
            val name = edtNameCategory.text.toString().trim()
            if (name.isEmpty()) {
                Toast.makeText(this@AddUpdateCategoryActivity, "Input tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return
            }

            val mCategoryRequest = CategoryRequest(name)
            createCategory(mCategoryRequest)
        }
    }

    private fun createCategory(mCategoryRequest: CategoryRequest) {
        viewModel.createCategoryProvider(mCategoryRequest).observe(this, { response ->
            when(response) {
                is MyResponse.Loading -> binding.buttonSave.showLoading()
                is MyResponse.Success -> {
                    binding.buttonSave.hideProgress(getString(R.string.save))
                    response.data?.meta?.let {
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
                is MyResponse.Error -> {
                    binding.buttonSave.hideProgress(getString(R.string.save))
                    Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}