package com.technzone.miniborsa.data.models

import androidx.lifecycle.MutableLiveData

data class SortModel(
    val title: String? = null,
    val type:Int? = null,
    val selected: MutableLiveData<Boolean> = MutableLiveData(false)
)