package com.technzone.minibursa.data.models.profile

import com.technzone.minibursa.data.enums.MoreEnums


data class More(val title: String, val icon: Int?, val moreEnums: MoreEnums, val notificationCount: String? = "0")