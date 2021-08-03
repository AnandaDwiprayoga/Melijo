package com.pasukanlangit.id.melijo.data.network.model.response

import com.google.gson.annotations.SerializedName

data class AllUserForProducerResponse(

	@field:SerializedName("result")
	val result: List<ProfileResult>,

	@field:SerializedName("meta")
	val meta: Meta
)


