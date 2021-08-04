package com.pasukanlangit.id.melijo.data.network.model.response

import com.google.gson.annotations.SerializedName

data class TransactionProducerResponse(

	@field:SerializedName("result")
	val result: List<TrxProducerResultItem>,

	@field:SerializedName("meta")
	val meta: Meta
)

data class TrxProducerResultItem(

	@field:SerializedName("id")
	val transactionId: Int,

	@field:SerializedName("total")
	val total: Int,

	@field:SerializedName("amount_of_goods")
	val amountOfGoods: Int,

	@field:SerializedName("status")
	val status: String,

	@field:SerializedName("created_at")
	val createdAt: String
)

