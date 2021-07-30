package com.pasukanlangit.id.melijo.presentation.mainprovider.home.category

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pasukanlangit.id.melijo.R
import com.pasukanlangit.id.melijo.databinding.ActivityCategoryProviderBinding
import com.pasukanlangit.id.melijo.utils.MyResponse
import com.pasukanlangit.id.melijo.utils.MyUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryProviderActivity : AppCompatActivity(R.layout.activity_category_provider) {

    private val binding: ActivityCategoryProviderBinding by viewBinding()
    private val viewModel: CategoryProviderViewModel by viewModels()
    private lateinit var mAdapter: CategoryProviderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MyUtils.setToolbarGreen(window, this)
        mAdapter = CategoryProviderAdapter(this)
        setupCategory()

        binding.buttonAddCategory.setOnClickListener {
            startActivity(Intent(this, AddUpdateCategoryActivity::class.java))
        }
        mAdapter.setOnDeleteButtonClickListener(object : CategoryProviderAdapter.OnDeleteButtonCategory {
            override fun onDeleteButtonClickListener(id: Int) {
                val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this@CategoryProviderActivity)
                alertDialog.setCancelable(false)
                alertDialog.setTitle("Konfirmasi Hapus Kategori")
                alertDialog.setMessage("Apakah anda yakin menghapus kategori ini?")
                alertDialog.setPositiveButton("Yakin") { dialog: DialogInterface, _ ->
                    deleteCategory(id)
                    dialog.dismiss()
                }
                alertDialog.setNegativeButton("Batal") { dialog: DialogInterface, _ ->
                    dialog.dismiss()
                }
                alertDialog.show()
            }
        })
    }

    private fun setupCategory() {
        with(binding.rvCategory) {
            layoutManager = LinearLayoutManager(this@CategoryProviderActivity)
            adapter = mAdapter
        }
        viewModel.categoryProvider.observe(this, { response ->
            binding.loading.isVisible = response is MyResponse.Loading
            when(response) {
                is MyResponse.Success -> {
                    response.data?.result?.let {
                        mAdapter.setCategoryProvider(it)
                        mAdapter.notifyDataSetChanged()
                    }
                    mAdapter.notifyDataSetChanged()
                }
                is  MyResponse.Error -> Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                else -> {}
            }
        })
    }

    private fun deleteCategory(id: Int) {
        viewModel.deleteCategoryProvider(id).observe(this, { response ->
            binding.loading.isVisible = response is MyResponse.Loading
            when(response) {
                is  MyResponse.Success -> {
                    response.data?.meta?.let {
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                        setupCategory()
                    }
                }
                is MyResponse.Error -> Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                else -> {}
            }
        })
    }

    override fun onResume() {
        super.onResume()
        setupCategory()
    }
}