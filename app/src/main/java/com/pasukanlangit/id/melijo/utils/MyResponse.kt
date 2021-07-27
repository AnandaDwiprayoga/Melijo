package com.pasukanlangit.id.melijo.utils

sealed class MyResponse<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : MyResponse<T>(data)
    class Loading<T>(data: T? = null) : MyResponse<T>(data)
    class Error<T>(message: String, data: T? = null) : MyResponse<T>(data, message)
}
