package com.pasukanlangit.id.melijo.data.network.model.response

import com.google.gson.annotations.SerializedName

data class AllProductSupplierResponse(

	@field:SerializedName("result")
	val result: List<ProductItem>,

	@field:SerializedName("meta")
	val meta: Meta
)
