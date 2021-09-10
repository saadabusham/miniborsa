package com.technzone.miniborsa.utils

import com.technzone.miniborsa.data.models.general.Countries

object CountryToPhonePrefix {

    fun List<Countries>.getCountryByCode(code: String): Countries {
        return this.singleOrNull { it.countryCode?.toLowerCase()?.contains(code.toLowerCase()) == true }
            ?: Countries(
                name = "Jordan", countryCode = "JOR",
                code = "+962"
            )
    }
}