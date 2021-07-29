package com.pasukanlangit.id.melijo.presentation.main.promo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pasukanlangit.id.melijo.data.network.model.response.PromoResultItem
import com.pasukanlangit.id.melijo.databinding.ItemPromoListBinding

class AllPromoAdapter(private var diskons: List<PromoResultItem>, private val listener: (PromoResultItem) -> Unit): RecyclerView.Adapter<AllPromoAdapter.MyViewHolder>() {
    inner class MyViewHolder(val binding: ItemPromoListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemPromoListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val diskon = diskons[position]
        with(holder.binding){
            tvTitle.text = "${diskon.name} (Rp ${diskon.discount})"
            tvDescription.text = diskon.description

            this.root.setOnClickListener {
                listener(diskon)
            }
        }
    }

    override fun getItemCount(): Int  = diskons.size
}