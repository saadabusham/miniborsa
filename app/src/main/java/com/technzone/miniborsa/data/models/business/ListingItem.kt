package com.technzone.miniborsa.data.models.business

import java.io.Serializable

data class ListingItem(
    val title: String,
    val description: String,
    val percent: Int?
):Serializable
