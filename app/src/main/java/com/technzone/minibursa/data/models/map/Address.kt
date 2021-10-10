package com.technzone.minibursa.data.models.map

import java.io.Serializable

data class Address(
    val lat: Double = 0.0,
    val lon: Double =0.0,
    var country: String ="",
    var city: String ="",
    var locationName: String =""
) : Serializable