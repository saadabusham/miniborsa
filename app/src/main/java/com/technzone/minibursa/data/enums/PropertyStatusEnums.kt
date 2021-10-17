package com.technzone.minibursa.data.enums


enum class PropertyStatusEnums(val value: Int) {
    FREEHOLD(1),
    LEASEHOLD(2),
    BOTH(3),
    NA(4);

    companion object {
        fun getStatusByValue(value: Int?): PropertyStatusEnums {
            return when (value) {
                1 -> FREEHOLD
                2 -> LEASEHOLD
                3 -> BOTH
                else -> NA
            }
        }
    }
}