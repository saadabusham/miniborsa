package com.technzone.minibursa.data.api.response

sealed class Result<out R> {
    data class Success<out T>(val data: T?) : Result<T>()
    data class Error(val throwable: Throwable) : Result<Nothing>()
    data class CustomError(
        val errorCode:Int?,
        val message: String
    ) : Result<Nothing>()
    object Loading : Result<Nothing>()
}