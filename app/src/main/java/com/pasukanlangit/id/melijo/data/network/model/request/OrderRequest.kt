package com.pasukanlangit.id.melijo.data.network.model.request

import com.google.gson.annotations.SerializedName

data class OrderRequest(

	@field:SerializedName("total_pay")
	val totalPay: Int,

	@field:SerializedName("address")
	val address: String,

	@field:SerializedName("products")
	val products: List<OrderProductsItem>
)

data class OrderProductsItem(

	@field:SerializedName("total")
	val total: Int,

	@field:SerializedName("quantity")
	val quantity: Int,

	@field:SerializedName("product_id")
	val productId: Int
)
