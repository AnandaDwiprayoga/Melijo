package com.pasukanlangit.id.melijo.presentation.main.home.supplier.product

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

class ProductSupplierAdapter(private val productItemEvent: ProductSellerDetailAdapter.ProductItemEvent) : ListAdapter<ProductItem, ProductSupplierAdapter.MyViewHolder>(DIFF_CALLBACK) {
    inner class MyViewHolder(val binding: ItemProductSupplierBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemProductSupplierBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var propMustShown = false
        val currentProduct = getItem(position)
        with(holder.binding){
            Glide.with(this.root)
                .load(currentProduct.picture)
                .circleCrop()
                .into(ivProduct)

            tvNameProduct.text = currentProduct.name
            tvPriceProduct.text = "Rp ${currentProduct.price}"
            labelAvailable.isVisible = currentProduct.stock > 0

            this.root.setOnClickListener {
                propMustShown = !propMustShown
                wrapperInputQty.isVisible = propMustShown
            }

            edtQty.setText(currentProduct.qty.toString())

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

                    currentList[position].qty = newQty
                    edtQty.setText(newQty.toString())

                    if(newQty == 1) productItemEvent.onAddToCart(currentList[position])
                    else productItemEvent.updateProductCart(currentList[position])
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

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProductItem>() {
            override fun areItemsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean =
                oldItem == newItem

        }
    }
}
