package com.pasukanlangit.id.melijo.presentation.mainprovider.home.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pasukanlangit.id.melijo.data.network.model.response.ProductItem
import com.pasukanlangit.id.melijo.databinding.ItemListProductBinding

class ProductProviderAdapter: RecyclerView.Adapter<ProductProviderAdapter.ProductsViewHolder>() {

    private var productsProvider = ArrayList<ProductItem>()

    fun setProductProvider(productsProvider: List<ProductItem>?) {
        if (productsProvider == null) return
        this.productsProvider.clear()
        this.productsProvider.addAll(productsProvider)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductProviderAdapter.ProductsViewHolder {
        return ProductsViewHolder(ItemListProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ProductProviderAdapter.ProductsViewHolder, position: Int) {
        val productItem = productsProvider[position]
        holder.bind(productItem)
    }

    override fun getItemCount(): Int = productsProvider.size

    inner class ProductsViewHolder(val binding: ItemListProductBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(productItem: ProductItem) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(productItem.picture)
                    .into(imgProduct)
                textNameProduct.text = productItem.name
                textPrice.text = productItem.price.toString()
            }
        }
    }
}