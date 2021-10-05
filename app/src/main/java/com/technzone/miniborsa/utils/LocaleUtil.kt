package com.technzone.miniborsa.utils

import java.util.*

class LocaleUtil {

    companion object {
        @JvmStatic
        fun getLanguage(): String {
            return getLocal().language
        }

        @JvmStatic
        fun getLocal(): Locale {
            return Locale.getDefault()
        }
    }
}