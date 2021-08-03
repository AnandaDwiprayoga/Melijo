package com.pasukanlangit.id.melijo.presentation.mainprovider.home.homebaseseller

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pasukanlangit.id.melijo.data.network.model.response.ProductItem
import com.pasukanlangit.id.melijo.databinding.ItemProductBaseSellerBinding

class ProductBaseSellerAdapter : ListAdapter<ProductItem, ProductBaseSellerAdapter.MyViewHolder>(DIFF_CALLBACK) {
    inner class MyViewHolder(val binding: ItemProductBaseSellerBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemProductBaseSellerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentProduct = getItem(position)

        with(holder.binding){
            Glide.with(this.root)
                .load(currentProduct.picture)
                .into(ivProduct)
            tvNameProduct.text = currentProduct.name
            tvPriceProduct.text = "Rp ${currentProduct.price}"
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