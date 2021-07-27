package com.pasukanlangit.id.melijo.data.network.model.response

import com.google.gson.annotations.SerializedName

data class LoginUserResponse(

	@field:SerializedName("result")
	val result: LoginResult,

	@field:SerializedName("meta")
	val meta: Meta
)

data class LoginData(

	@field:SerializedName("address")
	val address: String,

	@field:SerializedName("phoneNumber")
	val phoneNumber: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("photo")
	val photo: String,

	@field:SerializedName("email_verified_at")
	val emailVerifiedAt: Any,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("username")
	val username: String
)

data class LoginResult(

	@field:SerializedName("access_token")
	val accessToken: String,

	@field:SerializedName("data")
	val data: LoginData,

	@field:SerializedName("token_level")
	val tokenLevel: String,

	@field:SerializedName("token_type")
	val tokenType: String
)
