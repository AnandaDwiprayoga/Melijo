package com.pasukanlangit.id.melijo.presentation.main.home.seller

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pasukanlangit.id.melijo.data.network.model.request.UpdateLocationRequest
import com.pasukanlangit.id.melijo.data.network.model.response.DataSeller
import com.pasukanlangit.id.melijo.databinding.ItemListSellerBinding
import com.pasukanlangit.id.melijo.presentation.main.home.seller.detial.DetailSellerActivity
import com.pasukanlangit.id.melijo.utils.MyUtils

class SellersAdapter(private val locationUser: UpdateLocationRequest?) : ListAdapter<DataSeller, SellersAdapter.MyViewHolder>(DIFF_CALLBACK) {
    inner class MyViewHolder(val binding: ItemListSellerBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemListSellerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentSeller = getItem(position)
        with(holder.binding){
            val dummyDistance = if(locationUser == null) {
                MyUtils.getRandomDistance()
            }else{
                MyUtils.getKilometers(locationUser.latitude, locationUser.longitude, currentSeller.latitude, currentSeller.longitude)
            }

            Glide.with(this.root)
                .load(currentSeller.photo)
                .circleCrop()
                .into(ivSeller)
            tvNameSeller.text = currentSeller.name
            tvRating.text = currentSeller.rating.toString()
            tvDistance.text = "${dummyDistance}Km"

           with(this.root){
               setOnClickListener {
                   Intent(this.context, DetailSellerActivity::class.java).apply {
                       putExtra(DetailSellerActivity.KEY_ID_SELLER, currentSeller.id)
                       putExtra(DetailSellerActivity.DISTANCE_SELLER, dummyDistance)
                       @Suppress("LABEL_NAME_CLASH")
                       this@with.context.startActivity(this)
                   }
               }
           }
        }
    }

//    private fun Double.roundTo2Digits() : String {
//        return String.format("%.3f", this)
//    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataSeller>() {
            override fun areItemsTheSame(oldItem: DataSeller, newItem: DataSeller): Boolean =
                oldItem.phoneNumber == newItem.phoneNumber

            override fun areContentsTheSame(oldItem: DataSeller, newItem: DataSeller): Boolean =
                oldItem == newItem

        }
    }
}