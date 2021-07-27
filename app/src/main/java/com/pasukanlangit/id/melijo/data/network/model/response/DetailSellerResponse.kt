package com.pasukanlangit.id.melijo.data.network.model.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class DetailSellerResponse(

	@field:SerializedName("result")
	val result: DetailSellerResult,

	@field:SerializedName("meta")
	val meta: Meta
)

data class DetailSellerResult(

	@field:SerializedName("product")
	val product: List<ProductItem>,

	@field:SerializedName("bio")
	val bio: DataSeller
)

@Entity(tableName = "product")
data class ProductItem(

	@field:SerializedName("promo")
	val promo: String?,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("price")
	val price: Int,

	@field:SerializedName("name")
	val name: String,

	@PrimaryKey
	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("stock")
	val stock: Int,

	@field:SerializedName("picture")
	val picture: String,

	var qty : Int = 0
)


