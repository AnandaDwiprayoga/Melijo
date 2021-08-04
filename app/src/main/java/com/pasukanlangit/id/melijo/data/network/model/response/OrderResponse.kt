package com.pasukanlangit.id.melijo.data.network.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class OrderResponse(

	@field:SerializedName("result")
	val result: DataOrder,

	@field:SerializedName("meta")
	val meta: Meta
)

@Parcelize
data class OrdersItemResponse(

	@field:SerializedName("total")
	val total: Int,

	@field:SerializedName("quantity")
	val quantity: Int
) : Parcelable

@Parcelize
data class DataOrder(

	@field:SerializedName("total_pay")
	val totalPay: Int,

	@field:SerializedName("payment_url")
	val paymentUrl: String,

	@field:SerializedName("buyer_address")
	val buyerAddress: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("orders")
	val orders: List<OrdersItemResponse>,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("status")
	val status: String
) : Parcelable
