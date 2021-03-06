package com.pasukanlangit.id.melijo.presentation.mainprovider.home.homebasesup

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pasukanlangit.id.melijo.data.network.model.response.ProductItem
import com.pasukanlangit.id.melijo.databinding.ItemProductSupplierBinding
import com.pasukanlangit.id.melijo.presentation.main.home.seller.detial.ProductSellerDetailAdapter

class ProductBaseSupplierAdapter : ListAdapter<ProductItem, ProductBaseSupplierAdapter.MyViewHolder>(DIFF_CALLBACK) {
    inner class MyViewHolder(val binding: ItemProductSupplierBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemProductSupplierBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentProduct = getItem(position)
        with(holder.binding){
            Glide.with(this.root)
                .load(currentProduct.picture)
                .circleCrop()
                .into(ivProduct)

            tvNameProduct.text = currentProduct.name
            tvPriceProduct.text = "Rp ${currentProduct.price}"
            labelAvailable.isVisible = currentProduct.stock > 0
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProductItem>() {
            override fun areItemsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean =
                oldItem == newItem

        }
    }
}
