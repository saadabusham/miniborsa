package com.technzone.miniborsa.data.models.general

import androidx.lifecycle.MutableLiveData
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GeneralLookup(

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("name")
    val name: String?=null,

    @field:SerializedName("desc")
    val desc: String?=null,

    var selected: Boolean = false,
    @Transient
    val isSelected: MutableLiveData<Boolean> = MutableLiveData(false)
) : Serializable
