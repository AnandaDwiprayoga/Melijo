package com.pasukanlangit.id.melijo.data.network.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class UserProfileResponse(

	@field:SerializedName("result")
	val result: ProfileResult,

	@field:SerializedName("meta")
	val meta: Meta
)

@Parcelize
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
	val emailVerifiedAt: String?,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("username")
	val username: String,

	@field:SerializedName("longitude")
	val longitude: String
) : Parcelable
