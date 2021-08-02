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

