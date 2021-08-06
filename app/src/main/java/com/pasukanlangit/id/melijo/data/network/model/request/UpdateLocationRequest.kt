package com.pasukanlangit.id.melijo.data.network.model.request

import com.google.gson.annotations.SerializedName

data class UpdateLocationRequest(

	@field:SerializedName("latitude")
	val latitude: Double,

	@field:SerializedName("longitude")
	val longitude: Double
)
