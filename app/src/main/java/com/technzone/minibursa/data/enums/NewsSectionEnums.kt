package com.technzone.minibursa.data.enums


enum class NewsSectionEnums(val value: Int) {
    ALL(0),
    START_UP_NEWS(1),
    INVESTMENT(2),
    TIPS(3);

    companion object {
        fun getSectionByValue(value: Int): NewsSectionEnums {
            return when (value) {
                0 -> ALL
                1 -> START_UP_NEWS
                2 -> INVESTMENT
                else -> TIPS
            }
        }
    }
}