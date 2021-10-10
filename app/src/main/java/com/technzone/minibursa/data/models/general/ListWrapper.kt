package com.technzone.minibursa.data.models.general

data class ListWrapper<M>(
    val data: ArrayList<M>?,
    val totalRows: Int?
)