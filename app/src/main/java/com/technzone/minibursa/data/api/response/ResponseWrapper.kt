package com.technzone.minibursa.data.api.response

import com.google.gson.annotations.SerializedName

data class ResponseWrapper<RETURN_MODEL>(
    @field:SerializedName("success")
    val success: Boolean,
    @field:SerializedName("errorCode")
    val code: Int,
    @field:SerializedName("errorMessage")
    val message: String,
    @field:SerializedName("data")
    val data: RETURN_MODEL?
)