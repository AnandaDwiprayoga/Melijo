package com.pasukanlangit.id.melijo.data.network.model.response

import com.google.gson.annotations.SerializedName

data class MetaResponse(
	@field:SerializedName("meta")
	val meta: Meta
)

data class Meta(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)

data class MetaInputResponse(
	@field:SerializedName("meta")
	val meta: MetaInput
)

data class MetaInput(
	@field:SerializedName("message")
	val message:  List<MessageInputResponse>,
)
