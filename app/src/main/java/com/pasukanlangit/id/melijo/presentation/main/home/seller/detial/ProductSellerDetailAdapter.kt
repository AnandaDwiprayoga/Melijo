package com.pasukanlangit.id.melijo.presentation.main.home.seller.detial

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pasukanlangit.id.melijo.data.network.model.response.DataSeller
import com.pasukanlangit.id.melijo.data.network.model.response.ProductItem
import com.pasukanlangit.id.melijo.databinding.ItemListSellerBinding
import com.pasukanlangit.id.melijo.databinding.ItemProductDetailSellerBinding
import kotlin.random.Random

class ProductSellerDetailAdapter(private val productItemEvent: ProductItemEvent) : ListAdapter<ProductItem, ProductSellerDetailAdapter.MyViewHolder>(DIFF_CALLBACK) {
    inner class MyViewHolder(val binding: ItemProductDetailSellerBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemProductDetailSellerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentProduct = getItem(position)

        with(holder.binding){
            Glide.with(this.root)
                .load(currentProduct.picture)
                .into(ivProduct)
            tvNameProduct.text = currentProduct.name
            tvPriceProduct.text = currentProduct.price.toString()

            if(currentProduct.qty >= 1){
                wrapperQtyProduct.isVisible = true
                btnAddCart.isVisible = false
                edtQty.setText(currentProduct.qty.toString())
            }else{
                wrapperQtyProduct.isVisible = false
                btnAddCart.isVisible = true
            }

            btnAddCart.setOnClickListener {
                currentList[position].qty = 1
                wrapperQtyProduct.isVisible = true
                btnAddCart.isVisible = false

                productItemEvent.onAddToCart(currentList[position])
            }

            btnAdd.setOnClickListener {
                val currentQty = edtQty.text.toString().trim().toInt()
                if(currentProduct.stock > currentQty){
                    val newQty = currentQty.inc()

                    currentList[position].qty = newQty
                    edtQty.setText(newQty.toString())
                    productItemEvent.updateProductCart(currentList[position])
                    notifyDataSetChanged()
                }else{
                    Toast.makeText(this.root.context,"Product out of stock", Toast.LENGTH_SHORT).show()
                }
            }

            btnMinus.setOnClickListener {
                val currentQty = edtQty.text.toString().trim().toInt()
                if(currentQty > 1){
                    val newQty = currentQty.dec()

                    currentList[position].qty = newQty
                    edtQty.setText(newQty.toString())
                    productItemEvent.updateProductCart(currentList[position])
                    notifyDataSetChanged()
                }else{
                    productItemEvent.deleteProductCart(currentList[position])
                    notifyDataSetChanged()
                }
            }

        }
    }

//    private fun Double.roundTo2Digits() : String {
//        return String.format("%.3f", this)
//    }

    interface ProductItemEvent {
        fun onAddToCart(productItem: ProductItem)
        fun updateProductCart(productItem: ProductItem)
        fun deleteProductCart(productItem: ProductItem)
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