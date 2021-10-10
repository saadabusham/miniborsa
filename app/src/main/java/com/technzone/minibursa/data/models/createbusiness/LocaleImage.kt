package com.technzone.minibursa.data.models.createbusiness

import com.google.gson.annotations.SerializedName
import com.technzone.minibursa.data.enums.LocaleImageType
import java.io.Serializable

data class LocaleImage(

    @field:SerializedName("path")
    val path: String? = null,

    @field:SerializedName("contentType")
    val contentType: LocaleImageType? = null,

    @field:SerializedName("id")
    val id: Int? = null
) : Serializable