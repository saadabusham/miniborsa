package com.technzone.miniborsa.data.models.configuration

import com.squareup.moshi.Json

data class ConfigurationWrapperResponse(
    @field:Json(name ="configSetting")
    val configSetting: ConfigSetting? = null,

    @field:Json(name ="configString")
    val configString: ConfigString? = null,

    @field:Json(name ="updateStatus")
    val updateStatus: UpdateStatus? = null,

    @field:Json(name ="developer")
    val developer: Developer? = null,

    @field:Json(name ="company")
    val company: Company? = null,

    @field:Json(name ="id")
    val id: Int
    )


