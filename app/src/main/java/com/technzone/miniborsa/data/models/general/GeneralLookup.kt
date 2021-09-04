package com.technzone.miniborsa.data.models.general

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GeneralLookup(

    @field:SerializedName("Id")
    val id: Int? = null,

    @field:SerializedName("name")
    val name: String?=null,
    var selected: Boolean = false
) : Serializable
