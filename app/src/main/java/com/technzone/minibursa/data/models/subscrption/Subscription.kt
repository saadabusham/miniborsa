package com.technzone.minibursa.data.models.subscrption

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Subscription(

    @field:SerializedName("Id")
    val id: Int? = null,

    @field:SerializedName("name")
    val name: String?=null,

    @field:SerializedName("subscribe")
    val subscribe: String?=null,

    @field:SerializedName("promote")
    var promote: Boolean?=null,

    @field:SerializedName("price")
    val price: Double? = null,

    var selected: Boolean = false
) : Serializable
