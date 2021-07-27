package com.pasukanlangit.id.melijo.data.network.model.response

import com.google.gson.annotations.SerializedName

data class SellerListResponse(

	@field:SerializedName("result")
	val result: List<DataSeller>?,

	@field:SerializedName("meta")
	val meta: Meta
)

data class DataSeller(

	val id: Int,

	@field:SerializedName("address")
	val address: String,

	@field:SerializedName("phoneNumber")
	val phoneNumber: String,

	@field:SerializedName("is_active")
	val isActive: Boolean,

	@field:SerializedName("latitude")
	val latitude: Double,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("rating")
	val rating: Int,

	@field:SerializedName("photo")
	val photo: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("username")
	val username: String,

	@field:SerializedName("longitude")
	val longitude: Double
)
