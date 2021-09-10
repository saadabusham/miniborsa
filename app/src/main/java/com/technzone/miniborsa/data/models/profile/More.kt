package com.technzone.miniborsa.data.models.profile

import com.technzone.miniborsa.data.enums.MoreEnums


data class More(val title: String, val icon: Int?, val moreEnums: MoreEnums, val notificationCount: String? = "0")