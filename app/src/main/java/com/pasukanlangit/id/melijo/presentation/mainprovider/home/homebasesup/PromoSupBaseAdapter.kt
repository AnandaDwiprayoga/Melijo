package com.pasukanlangit.id.melijo.presentation.mainprovider.home.homebasesup

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.pasukanlangit.id.melijo.data.network.model.response.PromoResultItem
import com.pasukanlangit.id.melijo.databinding.ItemPromoListBinding

class PromoSupBaseAdapter(private var diskons: List<PromoResultItem>): RecyclerView.Adapter<PromoSupBaseAdapter.MyViewHolder>() {
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
        }
    }

    override fun getItemCount(): Int  = diskons.size
}