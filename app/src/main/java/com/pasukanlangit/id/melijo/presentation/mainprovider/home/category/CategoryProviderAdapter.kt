package com.pasukanlangit.id.melijo.presentation.mainprovider.home.category

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pasukanlangit.id.melijo.data.network.model.response.CategoryResultItem
import com.pasukanlangit.id.melijo.databinding.ItemListCategoryBinding

class CategoryProviderAdapter(private val activity: CategoryProviderActivity): RecyclerView.Adapter<CategoryProviderAdapter.CategoryViewHolder>() {

    private var categoryProvider = ArrayList<CategoryResultItem>()
    private lateinit var onDeleteButtonCategory: OnDeleteButtonCategory

    fun setCategoryProvider(categoryProvider: List<CategoryResultItem>?) {
        if (categoryProvider == null) return
        this.categoryProvider.clear()
        this.categoryProvider.addAll(categoryProvider)
    }

    fun setOnDeleteButtonClickListener(onDeleteButtonCategory: OnDeleteButtonCategory) {
        this.onDeleteButtonCategory = onDeleteButtonCategory
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryProviderAdapter.CategoryViewHolder {
        return CategoryViewHolder(ItemListCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: CategoryProviderAdapter.CategoryViewHolder, position: Int) {
        val categoryItem = categoryProvider[position]
        holder.bind(categoryItem)
        holder.binding.actionEdit.setOnClickListener {
            val intent = Intent(activity, AddUpdateCategoryActivity::class.java)
            intent.putExtra(AddUpdateCategoryActivity.UPDATE_KEY, categoryItem)
            activity.startActivity(intent)
        }
        holder.binding.actionDelete.setOnClickListener { onDeleteButtonCategory.onDeleteButtonClickListener(categoryProvider[holder.adapterPosition].id) }
    }

    override fun getItemCount(): Int = categoryProvider.size

    inner class CategoryViewHolder(val binding: ItemListCategoryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(category: CategoryResultItem) {
            with(binding) {
                textNameCategory.text = category.name
            }
        }
    }

    interface OnDeleteButtonCategory {
        fun onDeleteButtonClickListener(id: Int)
    }
}