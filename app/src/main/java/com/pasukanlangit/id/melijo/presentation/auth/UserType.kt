package com.pasukanlangit.id.melijo.presentation.auth

import java.io.Serializable


enum class UserType(val value: String) : Serializable {
    TYPE_BUYER("user"),
    TYPE_SELLER("seller"),
    TYPE_SUPPLIER("supplier")
}