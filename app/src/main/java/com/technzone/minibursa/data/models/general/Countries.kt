package com.technzone.minibursa.data.models.general

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Countries(
    @field:SerializedName("name")
    val name: String? = null,
    @field:SerializedName("countryCode")
    val countryCode: String? = null,
    @field:SerializedName("code")
    val code: String? = null,
    @field:SerializedName("regex")
    val regex: String? = null,
    var selected: Boolean = false
) : Serializable {
    override fun toString(): String {
        return "$code"
    }

    fun getDataString(): String {
        return "$name ($code)"
    }
}
