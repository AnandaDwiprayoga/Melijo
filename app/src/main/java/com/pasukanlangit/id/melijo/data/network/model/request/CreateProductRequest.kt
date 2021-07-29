package com.pasukanlangit.id.melijo.data.network.model.request

data class CreateProductRequest(
    val category_id: Int,
    val name: String,
    val price: Int,
    val stock: Int,
    val promo: Int = 0
)
