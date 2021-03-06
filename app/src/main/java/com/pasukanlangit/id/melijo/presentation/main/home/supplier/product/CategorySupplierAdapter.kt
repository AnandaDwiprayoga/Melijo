package com.pasukanlangit.id.melijo.presentation.main.home.supplier.product

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pasukanlangit.id.melijo.data.network.model.response.CategoryResultItem
import com.pasukanlangit.id.melijo.databinding.ItemCategorySupplierBinding

class CategorySupplierAdapter : ListAdapter<CategoryResultItem, CategorySupplierAdapter.MyViewHolder>(DIFF_CALLBACK) {
    inner class MyViewHolder(val binding: ItemCategorySupplierBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemCategorySupplierBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentCategory = getItem(position)
        with(holder.binding){
            tvNameCategory.text = currentCategory.name

            val root = this.root
            root.setOnClickListener {
                Intent(root.context, ProductByCategorySupActivity::class.java).apply {
                    putExtra(ProductByCategorySupActivity.KEY_CATEGORY, currentCategory)
                    root.context.startActivity(this)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CategoryResultItem>() {
            override fun areItemsTheSame(oldItem: CategoryResultItem, newItem: CategoryResultItem): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: CategoryResultItem, newItem: CategoryResultItem): Boolean =
                oldItem == newItem

        }
    }
}


