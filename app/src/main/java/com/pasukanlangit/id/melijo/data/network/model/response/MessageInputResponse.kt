package com.pasukanlangit.id.melijo.data.network.model.response

import com.google.gson.annotations.SerializedName

data class MessageInputResponse(

	@field:SerializedName("field")
	val field: String,

	@field:SerializedName("errors")
	val errors: List<String>
)
