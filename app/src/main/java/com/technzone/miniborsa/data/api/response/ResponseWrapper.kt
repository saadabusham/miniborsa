package com.technzone.miniborsa.data.api.response

import com.squareup.moshi.Json

data class ResponseWrapper<RETURN_MODEL>(
    @field:Json(name = "success")
    val success: Boolean,
    @field:Json(name = "errorCode")
    val code: Int,
    @field:Json(name = "errorMessage")
    val message: String,
    @field:Json(name = "data")
    val data: RETURN_MODEL?
)