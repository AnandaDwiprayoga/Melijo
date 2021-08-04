package com.pasukanlangit.id.melijo.presentation.main.order

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pasukanlangit.id.melijo.data.network.model.response.ResultItem
import com.pasukanlangit.id.melijo.databinding.ItemOrderBuyerBinding
import com.pasukanlangit.id.melijo.presentation.main.order.detail.DetailTrxBuyerActivity
import java.text.SimpleDateFormat
import java.util.*

class OrderBuyerAdapter : ListAdapter<ResultItem, OrderBuyerAdapter.MyViewHolder>(DIFF_CALLBACK) {
    inner class MyViewHolder(val binding: ItemOrderBuyerBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemOrderBuyerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentOrder = getItem(position)
        with(holder.binding){
            val root = this.root
            val context = this.root.context

            val dateFormatted = SimpleDateFormat("dd MMM yyyy - hh:mm:ss", Locale.US).parse(currentOrder.createdAt)
            val shortDateWithId = SimpleDateFormat("dd-mm-yyyy").format(dateFormatted).replace("-","").plus(currentOrder.id)

            tvIdOrder.text = "TRX$shortDateWithId"
            tvCountOrder.text = "${currentOrder.amountGoods} Barang"
            tvPriceOrder.text = "Rp ${currentOrder.totalPay}"
            tvStatusOrder.text = "Status : ${currentOrder.status}"

            root.setOnClickListener {
                with(Intent(context, DetailTrxBuyerActivity::class.java)){
                    putExtra(DetailTrxBuyerActivity.KEY_TRX_ID, currentOrder.id)
                    context.startActivity(this)
                }
            }
        }
    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ResultItem>() {
            override fun areItemsTheSame(oldItem: ResultItem, newItem: ResultItem): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ResultItem, newItem: ResultItem): Boolean =
                oldItem == newItem

        }
    }
}