package com.technzone.miniborsa.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Media(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("contentType")
    val contentType: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,
) : Serializable