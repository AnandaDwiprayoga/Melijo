package com.pasukanlangit.id.melijo.presentation.main.checkout.seller

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

class ProductCheckoutAdapter(private var items: List<ProductItem>, private val productItemEvent: ProductItemEvent) : RecyclerView.Adapter<ProductCheckoutAdapter.MyViewHolder>(){
    inner class MyViewHolder(val binding: ItemProductDetailSellerBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemProductDetailSellerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    fun setNewItems(items: List<ProductItem>){
        this.items = items
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentProduct = items[position]

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

//            btnAddCart.setOnClickListener {
//                currentList[position].qty = 1
//                wrapperQtyProduct.isVisible = true
//                btnAddCart.isVisible = false
//
//                productItemEvent.onAddToCart(currentList[position])
//            }

            btnAdd.setOnClickListener {
                val currentQty = edtQty.text.toString().trim().toInt()
                if(currentProduct.stock > currentQty){
                    val newQty = currentQty.inc()

                    items[position].qty = newQty
                    edtQty.setText(newQty.toString())
                    productItemEvent.updateProductCart(items[position])
                    notifyDataSetChanged()
                }else{
                    Toast.makeText(this.root.context,"Product out of stock", Toast.LENGTH_SHORT).show()
                }
            }

            btnMinus.setOnClickListener {
                val currentQty = edtQty.text.toString().trim().toInt()
                if(currentQty > 1){
                    val newQty = currentQty.dec()

                    items[position].qty = newQty
                    edtQty.setText(newQty.toString())
                    productItemEvent.updateProductCart(items[position])
                    notifyDataSetChanged()
                }else{
                    productItemEvent.deleteProductCart(items[position])
                    notifyDataSetChanged()
                }
            }

        }
    }

//    private fun Double.roundTo2Digits() : String {
//        return String.format("%.3f", this)
//    }

    interface ProductItemEvent {
//        fun onAddToCart(productItem: ProductItem)
        fun updateProductCart(productItem: ProductItem)
        fun deleteProductCart(productItem: ProductItem)
    }

    override fun getItemCount(): Int = items.size
}