package com.technzone.miniborsa.data.models.news

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BusinessNews(

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("type")
    val type: Int? = null,

    @field:SerializedName("section")
    val section: Int? = null,

    @field:SerializedName("serviceId")
    val serviceId: Int? = null,

    @field:SerializedName("value")
    val value: String? = null
) : Serializable