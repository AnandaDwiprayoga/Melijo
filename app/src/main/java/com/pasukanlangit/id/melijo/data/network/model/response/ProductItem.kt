package com.pasukanlangit.id.melijo.data.network.model.response

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "product")
data class ProductItem(

	@field:SerializedName("promo")
	val promo: Int,

	@field:SerializedName("category_id")
	val categoryId: Int,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("price")
	val price: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("description")
	val description: String?,

	@PrimaryKey
	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("stock")
	val stock: Int,

	@field:SerializedName("picture")
	val picture: String,

	var qty : Int = 0,

	var ownerId: Int = -1
): Parcelable
