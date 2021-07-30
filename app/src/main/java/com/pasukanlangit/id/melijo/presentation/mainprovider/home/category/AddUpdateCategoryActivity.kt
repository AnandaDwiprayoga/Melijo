package com.pasukanlangit.id.melijo.presentation.mainprovider.home.category

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.razir.progressbutton.hideProgress
import com.pasukanlangit.id.melijo.R
import com.pasukanlangit.id.melijo.data.network.model.request.CategoryRequest
import com.pasukanlangit.id.melijo.data.network.model.response.CategoryResultItem
import com.pasukanlangit.id.melijo.databinding.ActivityAddUpdateCategoryBinding
import com.pasukanlangit.id.melijo.utils.MyResponse
import com.pasukanlangit.id.melijo.utils.MyUtils
import com.pasukanlangit.id.melijo.utils.showLoading
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddUpdateCategoryActivity : AppCompatActivity(R.layout.activity_add_update_category) {

    companion object {
        const val UPDATE_KEY = "update_key"
    }

    private val binding: ActivityAddUpdateCategoryBinding by viewBinding()
    private val viewModel: CategoryProviderViewModel by viewModels()
    private var categoryItem: CategoryResultItem ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MyUtils.setToolbarGreen(window, this)

        categoryItem = intent.getParcelableExtra(UPDATE_KEY)
        with(binding) {
            if (categoryItem != null) {
                textTitle.text = getString(R.string.title_update_category)
                edtNameCategory.setText(categoryItem?.name)
            } else {
                textTitle.text = getString(R.string.title_add_category)
                edtNameCategory.setText("")
            }
            buttonCancel.setOnClickListener{ finish() }
            backToCategory.setOnClickListener { finish() }
            buttonSave.setOnClickListener { createOrUpdateCategoryProcess() }
        }
    }

    private fun createOrUpdateCategoryProcess() {
        with(binding) {
            val name = edtNameCategory.text.toString().trim()
            if (name.isEmpty()) {
                Toast.makeText(this@AddUpdateCategoryActivity, "Input tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return
            }

            val mCategoryRequest = CategoryRequest(name)
            createOrUpdateCategory(mCategoryRequest)
        }
    }

    private fun createOrUpdateCategory(mCategoryRequest: CategoryRequest) {
        if (categoryItem != null) {
            categoryItem?.id?.let { id ->
                viewModel.updateCategoryProvider(id, mCategoryRequest).observe(this, { response ->
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
        } else {
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
}