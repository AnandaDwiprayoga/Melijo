package com.pasukanlangit.id.melijo.data.network.model.response

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class AllPromoResponse(

	@field:SerializedName("result")
	val result: List<PromoResultItem>,

	@field:SerializedName("meta")
	val meta: Meta
)

@Parcelize
@Entity(tableName = "promo")
data class PromoResultItem(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("producer_id")
	val producerId: Int,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("discount")
	val discount: Int,

	@PrimaryKey
	@field:SerializedName("id")
	val id: Int,

	var isMain: Boolean = false
): Parcelable
