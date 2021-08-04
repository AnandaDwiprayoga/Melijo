package com.pasukanlangit.id.melijo.data.network.model.response

import com.google.gson.annotations.SerializedName

data class DetailTransactionForProducerResponse(

	@field:SerializedName("result")
	val result: DetailTrxResult,

	@field:SerializedName("meta")
	val meta: Meta
)

data class DetailTrxResult(

	@field:SerializedName("total")
	val total: Int,

	@field:SerializedName("address")
	val address: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("photo")
	val photo: String,

	@field:SerializedName("orders")
	val orders: List<DetailTrxProduct>
)

data class DetailTrxProduct(

	@field:SerializedName("total")
	val total: Int,

	@field:SerializedName("quantity")
	val quantity: Int,

	@field:SerializedName("product_id")
	val productId: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("picture")
	val picture: String,

	@field:SerializedName("status")
	val status: String?
)

