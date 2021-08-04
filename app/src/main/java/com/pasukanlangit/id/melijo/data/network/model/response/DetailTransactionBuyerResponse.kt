package com.pasukanlangit.id.melijo.data.network.model.response

import com.google.gson.annotations.SerializedName

data class DetailTransactionBuyerResponse(

	@field:SerializedName("result")
	val result: DetailTrxBuyerResult,

	@field:SerializedName("meta")
	val meta: Meta
)

data class DetailTrxBuyerResult(

	@field:SerializedName("payment_url")
	val paymentUrl: String,

	@field:SerializedName("total")
	val total: Int,

	@field:SerializedName("address")
	val address: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("photo")
	val photo: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("orders")
	val orders: List<DetailTrxProduct>,

	@field:SerializedName("status")
	val status: String
)


