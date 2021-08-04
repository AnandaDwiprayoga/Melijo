package com.pasukanlangit.id.melijo.data.network.model.response

import com.google.gson.annotations.SerializedName

data class OrderBuyerResponse(

	@field:SerializedName("result")
	val result: List<ResultItem>,

	@field:SerializedName("meta")
	val meta: Meta
)

data class ResultItem(

	@field:SerializedName("total_pay")
	val totalPay: Int,

	@field:SerializedName("payment_url")
	val paymentUrl: String,

	@field:SerializedName("amount_of_goods")
	val amountGoods: String,

	@field:SerializedName("buyer_address")
	val buyerAddress: String,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("status")
	val status: String
)
