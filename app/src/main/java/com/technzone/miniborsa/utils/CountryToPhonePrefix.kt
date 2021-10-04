package com.technzone.miniborsa.utils

import com.technzone.miniborsa.data.models.general.Countries

object CountryToPhonePrefix {

    fun List<Countries>.getCountryByCountryISO(code: String): Countries {
        return this.singleOrNull { it.countryCode?.toLowerCase()?.contains(code.toLowerCase()) == true }
            ?: Countries(
                name = "Jordan", countryCode = "JOR",
                code = "+962"
            )
    }
    fun List<Countries>.getCodeByPhoneNumber(phoneNumber: String): Countries {
        return this.singleOrNull { phoneNumber.startsWith(it.code?.toLowerCase()?:"xxx") }
            ?: Countries(
                name = "Jordan", countryCode = "JOR",
                code = "+962"
            )
    }
}