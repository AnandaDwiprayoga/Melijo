package com.pasukanlangit.id.melijo.presentation.mainprovider.home.promo

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pasukanlangit.id.melijo.data.network.model.response.PromoResultItem
import com.pasukanlangit.id.melijo.databinding.ItemListPromoBinding

class PromoProviderAdapter(private val activity: PromoProviderActivity): RecyclerView.Adapter<PromoProviderAdapter.PromoViewHolder>() {

    private var promoItems = ArrayList<PromoResultItem>()
    private lateinit var onDeleteButtonClickListener: OnDeleteButtonClickListener

    fun setPromoProvider(promoItems: List<PromoResultItem>?) {
        if (promoItems == null) return
        this.promoItems.clear()
        this.promoItems.addAll(promoItems)
    }

    fun setOnClickDeleteButtonListener(onDeleteButtonClickListener: OnDeleteButtonClickListener) {
        this.onDeleteButtonClickListener = onDeleteButtonClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromoProviderAdapter.PromoViewHolder {
        return PromoViewHolder(ItemListPromoBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PromoProviderAdapter.PromoViewHolder, position: Int) {
        val promoItem = promoItems[position]
        holder.bind(promoItem)
        holder.binding.actionEdit.setOnClickListener {
            val intent = Intent(activity, AddUpdatePromoProviderActivity::class.java)
            intent.putExtra(AddUpdatePromoProviderActivity.UPDATE_KEY, promoItem)
            activity.startActivity(intent)
        }
        holder.binding.actionDelete.setOnClickListener { onDeleteButtonClickListener.onDeleteButtonClicked(promoItems[holder.adapterPosition].id) }
    }

    override fun getItemCount(): Int = promoItems.size

    inner class PromoViewHolder(val binding: ItemListPromoBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(promoItem: PromoResultItem) {
            with(binding) {
                textNamePromo.text = promoItem.name
            }
        }
    }

    interface OnDeleteButtonClickListener {
        fun onDeleteButtonClicked(promoId: Int)
    }
}