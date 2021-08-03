package com.pasukanlangit.id.melijo.data.network.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class ProfilProducerResponse(

	@field:SerializedName("result")
	val result: LoginProducerData,

	@field:SerializedName("meta")
	val meta: Meta
)

@Parcelize
data class LoginProducerData(

	@field:SerializedName("address")
	val address: String,

	@field:SerializedName("phoneNumber")
	val phoneNumber: String,

	@field:SerializedName("is_active")
	val isActive: Boolean,

	@field:SerializedName("latitude")
	val latitude: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("rating")
	val rating: Int,

	@field:SerializedName("photo")
	val photo: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("username")
	val username: String,

	@field:SerializedName("longitude")
	val longitude: String
) : Parcelable
