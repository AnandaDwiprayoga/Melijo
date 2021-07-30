package com.pasukanlangit.id.melijo.presentation.mainprovider.home.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pasukanlangit.id.melijo.data.network.model.response.CategoryResultItem
import com.pasukanlangit.id.melijo.databinding.ItemListCategoryBinding

class CategoryProviderAdapter: RecyclerView.Adapter<CategoryProviderAdapter.CategoryViewHolder>() {

    private var categoryProvider = ArrayList<CategoryResultItem>()

    fun setCategoryProvider(categoryProvider: List<CategoryResultItem>?) {
        if (categoryProvider == null) return
        this.categoryProvider.clear()
        this.categoryProvider.addAll(categoryProvider)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryProviderAdapter.CategoryViewHolder {
        return CategoryViewHolder(ItemListCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: CategoryProviderAdapter.CategoryViewHolder, position: Int) {
        val categoryItem = categoryProvider[position]
        holder.bind(categoryItem)
    }

    override fun getItemCount(): Int = categoryProvider.size

    inner class CategoryViewHolder(val binding: ItemListCategoryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(category: CategoryResultItem) {
            with(binding) {
                textNameCategory.text = category.name
            }
        }
    }
}