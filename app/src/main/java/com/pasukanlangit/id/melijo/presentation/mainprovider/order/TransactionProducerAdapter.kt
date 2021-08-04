package com.pasukanlangit.id.melijo.presentation.mainprovider.order

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
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
import com.pasukanlangit.id.melijo.R
import com.pasukanlangit.id.melijo.data.network.model.response.TrxProducerResultItem
import com.pasukanlangit.id.melijo.databinding.ItemTransactionProducerBinding
import com.pasukanlangit.id.melijo.presentation.mainprovider.order.detail.DetailTrxProducerActivity
import java.text.SimpleDateFormat
import java.util.*

class TransactionProducerAdapter(private val btnActionClicked: (Int, Int) -> Unit) : ListAdapter<TrxProducerResultItem, TransactionProducerAdapter.MyViewHolder>(DIFF_CALLBACK) {
    inner class MyViewHolder(val binding: ItemTransactionProducerBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemTransactionProducerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentOrder = getItem(position)
        with(holder.binding){
            val root = this.root
            val context = this.root.context

            val dateFormatted = SimpleDateFormat("yyyy-mm-dd hh:mm:ss", Locale.US).parse(currentOrder.createdAt)
            val shortDateWithId = SimpleDateFormat("dd-mm-yyyy").format(dateFormatted).replace("-","").plus(currentOrder.transactionId)

            tvIdProduct.text = "TRX${shortDateWithId}"
            tvPriceProduct.text = "Rp ${currentOrder.total}"
            tvAmountProduct.text = "${currentOrder.amountOfGoods} Barang"

            btnAction.isVisible = !currentOrder.status.equals(StatusTransaction.DONE.value, true)
            labelCompleted.isVisible = currentOrder.status.equals(StatusTransaction.DONE.value, true)

            if(currentOrder.status.equals(StatusTransaction.PENDING.value, true)){
                setButtonAction("in process", Color.parseColor("#FFFF66"),btnAction)
            }else if(currentOrder.status.equals(StatusTransaction.IN_PROCESS.value, true)){
                setButtonAction("done", context.getThemeColor(R.attr.colorSecondaryVariant),btnAction)
            }

            btnAction.setOnClickListener {
                btnActionClicked(StatusTransaction.values().single { status -> status.value.equals(currentOrder.status, true) }.ordinal + 1, currentOrder.transactionId)
            }

            root.setOnClickListener {
                with(Intent(context, DetailTrxProducerActivity::class.java)){
                    putExtra(DetailTrxProducerActivity.KEY_TRX_ID, currentOrder.transactionId)
                    context.startActivity(this)
                }
            }
        }
    }

    private fun setButtonAction(text: String, color: Int, btn: Button) {
        btn.text = text
        btn.setBackgroundColor(color)
    }

    private fun Context.getThemeColor(@AttrRes attrRes: Int): Int {
        val typedValue = TypedValue()
        theme.resolveAttribute (attrRes, typedValue, true)
        return typedValue.data
    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TrxProducerResultItem>() {
            override fun areItemsTheSame(oldItem: TrxProducerResultItem, newItem: TrxProducerResultItem): Boolean =
                oldItem.transactionId == newItem.transactionId

            override fun areContentsTheSame(oldItem: TrxProducerResultItem, newItem: TrxProducerResultItem): Boolean =
                oldItem == newItem

        }
    }
}