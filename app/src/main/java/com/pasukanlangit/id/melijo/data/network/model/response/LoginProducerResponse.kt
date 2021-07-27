package com.pasukanlangit.id.melijo.data.network.model.response

import com.google.gson.annotations.SerializedName

data class LoginProducerResponse(

	@field:SerializedName("result")
	val result: Result,

	@field:SerializedName("meta")
	val meta: Meta
)


data class Result(

	@field:SerializedName("access_token")
	val accessToken: String,

	@field:SerializedName("data")
	val data: LoginProducerData,

	@field:SerializedName("token_level")
	val tokenLevel: String,

	@field:SerializedName("token_type")
	val tokenType: String
)

data class LoginProducerData(

	@field:SerializedName("address")
	val address: String,

	@field:SerializedName("phoneNumber")
	val phoneNumber: String,

	@field:SerializedName("is_active")
	val isActive: Boolean,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("rating")
	val rating: Any,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("username")
	val username: String
)
