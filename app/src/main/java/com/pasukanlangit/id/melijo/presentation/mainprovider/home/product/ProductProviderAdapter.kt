package com.pasukanlangit.id.melijo.presentation.mainprovider.home.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pasukanlangit.id.melijo.R
import com.pasukanlangit.id.melijo.data.network.model.response.ProductItem
import com.pasukanlangit.id.melijo.databinding.ItemListProductBinding
import java.lang.NumberFormatException
import java.text.DecimalFormat

class ProductProviderAdapter: RecyclerView.Adapter<ProductProviderAdapter.ProductsViewHolder>() {

    private var productsProvider = ArrayList<ProductItem>()
    private lateinit var onDeleteButtonProduct: OnDeleteButtonProduct
    private lateinit var onEditButtonProduct: OnEditButtonProduct

    fun setProductProvider(productsProvider: List<ProductItem>?) {
        if (productsProvider == null) return
        this.productsProvider.clear()
        this.productsProvider.addAll(productsProvider)
    }

    fun setOnEditButtonClickLister(onEditButtonProduct: OnEditButtonProduct) {
        this.onEditButtonProduct = onEditButtonProduct
    }

    fun setOnDeleteButtonClickListener(onDeleteButtonProduct: OnDeleteButtonProduct) {
        this.onDeleteButtonProduct = onDeleteButtonProduct
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductProviderAdapter.ProductsViewHolder {
        return ProductsViewHolder(ItemListProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ProductProviderAdapter.ProductsViewHolder, position: Int) {
        val productItem = productsProvider[position]
        holder.bind(productItem)

        holder.binding.actionEdit.setOnClickListener { onEditButtonProduct.onEditButtonClickListener(productsProvider[holder.adapterPosition]) }
        holder.binding.actionDelete.setOnClickListener { onDeleteButtonProduct.onDeleteButtonClickListener(productsProvider[holder.adapterPosition].id) }
    }

    override fun getItemCount(): Int = productsProvider.size

    inner class ProductsViewHolder(val binding: ItemListProductBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(productItem: ProductItem) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(productItem.picture)
                    .into(imgProduct)
                textNameProduct.text = productItem.name
                textPrice.text = this.root.resources.getString(R.string.format_price, formatPrice(productItem.price))
            }
        }

        private fun formatPrice(price: Int): String {
            try {
                val formatter = DecimalFormat("#,###,###")
                return formatter.format(price)
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
            return ""
        }
    }

    interface OnDeleteButtonProduct {
        fun onDeleteButtonClickListener(id: Int)
    }

    interface OnEditButtonProduct {
        fun onEditButtonClickListener(item: ProductItem)
    }
}