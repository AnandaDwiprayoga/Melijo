package com.pasukanlangit.id.melijo.data.network.model.response

import com.google.gson.annotations.SerializedName

data class UserProfileResponse(

	@field:SerializedName("result")
	val result: ProfileResult,

	@field:SerializedName("meta")
	val meta: Meta
)

data class ProfileResult(

	@field:SerializedName("address")
	val address: String,

	@field:SerializedName("phoneNumber")
	val phoneNumber: String,

	@field:SerializedName("latitude")
	val latitude: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("photo")
	val photo: String,

	@field:SerializedName("email_verified_at")
	val emailVerifiedAt: Any,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("username")
	val username: String,

	@field:SerializedName("longitude")
	val longitude: String
)
