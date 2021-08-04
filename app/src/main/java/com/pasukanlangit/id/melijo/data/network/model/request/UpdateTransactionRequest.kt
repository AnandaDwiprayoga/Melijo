package com.pasukanlangit.id.melijo.data.network.model.request

import com.google.gson.annotations.SerializedName

data class UpdateTransactionRequest(

	@field:SerializedName("status")
	val status: String
)
