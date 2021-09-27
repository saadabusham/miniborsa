package com.technzone.miniborsa.utils.extensions

import com.technzone.miniborsa.data.models.business.business.OwnerBusiness

fun OwnerBusiness.calculatePercentage(): Int {
    var percentage = 0
    percentage += calculateFirstStepPercentage()
    percentage += calculateSecondStepPercentage()
    percentage += calculateThirdPercentage()
    percentage += calculateFourthPercentage()
    return percentage
}

fun OwnerBusiness.calculateFirstStepPercentage(): Int {
    var percentage = 0
    //step one
    if (!title.isNullOrEmpty()) {
        percentage += 10
    }
    if (!englishDescription.isNullOrEmpty()) {
        percentage += 10
    }
    if (categories?.size ?: 0 > 0) {
        percentage += 10
    }
    if (categories?.size ?: 0 > 1) {
        percentage += 5
    }
    if (categories?.size ?: 0 > 2) {
        percentage += 5
    }
    if (!establishedYear.isNullOrEmpty()) {
        percentage += 10
    }
    if (!address.isNullOrEmpty()) {
        percentage += 10
    }
    return percentage
}

fun OwnerBusiness.calculateSecondStepPercentage(): Int {
    var percentage = 0
    //step two
    if (askingPrice ?: 0 > 0 || askingPriceNA == true || askingPriceBoth ?: 0 > 0.0 || askingPriceNABoth == true) {
        percentage += 10
    }
    if (annualNetProfit ?: 0 > 0.0 || annualTurnoverNA == true) {
        percentage += 5
    }
    if (annualTurnover ?: 0 > 0.0 || annualTurnoverNA == true) {
        percentage += 5
    }
    return percentage
}

fun OwnerBusiness.calculateThirdPercentage(): Int {
    var percentage = 0
    //step three
    if (!images.isNullOrEmpty()) {
        percentage += 5
    }
    if (!files.isNullOrEmpty()) {
        percentage += 5
    }
    return percentage
}

fun OwnerBusiness.calculateFourthPercentage(): Int {
    var percentage = 0
    //step four
    if (!websiteLink.isNullOrEmpty()) {
        percentage += 5
    }
    if (!properties.isNullOrEmpty()) {
        percentage += 5
    }
    return percentage
}