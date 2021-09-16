package com.technzone.miniborsa.data.models.general

data class ListWrapper<M>(
    val data: ArrayList<M>?,
    val totalRows: Int?
)