package com.pasukanlangit.id.melijo.presentation.mainprovider.order.detail

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.AttrRes
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pasukanlangit.id.melijo.R
import com.pasukanlangit.id.melijo.data.network.model.response.DetailTrxProduct
import com.pasukanlangit.id.melijo.data.network.model.response.TrxProducerResultItem
import com.pasukanlangit.id.melijo.databinding.ItemDetailProductTrxBinding
import com.pasukanlangit.id.melijo.databinding.ItemTransactionProducerBinding
import java.text.SimpleDateFormat
import java.util.*

class ProductDetailTrxAdapter : ListAdapter<DetailTrxProduct, ProductDetailTrxAdapter.MyViewHolder>(DIFF_CALLBACK) {
    inner class MyViewHolder(val binding: ItemDetailProductTrxBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemDetailProductTrxBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val product = getItem(position)
        with(holder.binding){
            Glide.with(this.root)
                .load(product.picture)
                .into(ivProduct)
            tvNameProduct.text = product.name
            tvCountProduct.text = "Jumlah ${product.quantity}"
            tvPrice.text = "Rp ${product.total}"
            tvStatus.isVisible = product.status != null
            tvStatus.text = "Status : ${product.status}"

        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DetailTrxProduct>() {
            override fun areItemsTheSame(oldItem: DetailTrxProduct, newItem: DetailTrxProduct): Boolean =
                oldItem.productId == newItem.productId

            override fun areContentsTheSame(oldItem: DetailTrxProduct, newItem: DetailTrxProduct): Boolean =
                oldItem == newItem

        }
    }
}