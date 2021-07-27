package com.pasukanlangit.id.melijo.data.network.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class CategoryResponse(

	@field:SerializedName("result")
	val result: List<CategoryResultItem>,

	@field:SerializedName("meta")
	val meta: Meta
)

@Parcelize
data class CategoryResultItem(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int
) : Parcelable
