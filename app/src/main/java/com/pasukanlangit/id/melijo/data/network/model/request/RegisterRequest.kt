package com.pasukanlangit.id.melijo.data.network.model.request

data class RegisterRequest (
    val username: String,
    val name: String,
    val email: String,
    val password: String,
    val address: String,
    val phoneNumber: String
)