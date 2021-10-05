package com.technzone.miniborsa.utils.extensions

import android.content.Context
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.technzone.miniborsa.utils.DateTimeUtil
import com.technzone.miniborsa.utils.validation.Validator
import com.technzone.miniborsa.utils.validation.ValidatorInputTypesEnums
import java.math.BigDecimal
import java.net.URLDecoder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.roundToInt

fun String?.toDate(format: String = DateTimeUtil.FULL_DATE_TIME_FORMATTING): Date? {
    if (this == null) return null
    return Date(DateTimeUtil.getTimeStampFromStringDate(this, format))
}

fun String.validate(validatorInputTypesEnums: ValidatorInputTypesEnums, context: Context): Validator.ValidatedData {
    return Validator().validate(validatorInputTypesEnums, this, context)
}

fun String.validateConfirmPassword(
    validatorInputTypesEnums: ValidatorInputTypesEnums,
    passwordToMatch: String,
    context: Context
): Validator.ValidatedData {
    return Validator().validate(validatorInputTypesEnums, this, passwordToMatch, context)
}

fun String?.getCalendarDay(): CalendarDay {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
    inputFormat.timeZone = TimeZone.getTimeZone("UTC")
    val outputFormat = SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.ENGLISH)
    outputFormat.timeZone = TimeZone.getDefault()
    val date = outputFormat.format(inputFormat.parse(this))

    return CalendarDay.from(
        date.substring(0, 4).toInt(),
        date.substring(5, 7).toInt(),
        date.substring(8, 10).toInt()
    )
}


fun String?.toUtf8(): String {
    return URLDecoder.decode(this, "UTF-8")
}

fun String.toInteger(): Int {
    return try {
        toInt()
    } catch (e: Exception) {
        return 0
    }
}

fun String.replaceArabicDigitsWithEnglish() :String{
    var result = ""
    var en = '0'
    for (ch in this) {
        en = ch
        when (ch) {
            '۰' -> en = '0'
            '۱' -> en = '1'
            '۲' -> en = '2'
            '۳' -> en = '3'
            '۴' -> en = '4'
            '۵' -> en = '5'
            '۶' -> en = '6'
            '۷' -> en = '7'
            '۸' -> en = '8'
            '۹' -> en = '9'
        }
        result = "${result}$en"
    }
    return result
}

fun String.checkPhoneNumberFormat(): String {
    val mobile = when {
        this.startsWith("00962") -> this.replaceFirst("00962", "")
        this.startsWith("962") -> this.replaceFirst("962", "")
        this.startsWith("+962") -> this.replaceFirst("+962", "")
        else -> this
    }

    return if (mobile.startsWith("07")) {
        mobile.replaceFirst("07", "7")
    } else {
        mobile
    }
}

fun Double.round(digitNum: Int): String {
    return String.format(Locale.ENGLISH,"%.${digitNum}f", this)
}

fun String.toPriceOrNull():Double?{
    if(this.isNullOrEmpty())
        return null
    return try {
        if(this.contains(".")){
            this.toDoubleOrNull()
        }else{
            withMathRound(this.toDouble(),this.count { it.equals(".") })
//            this.toDouble().roundTo2DecimalPlaces(1)
        }
    }catch (e: NumberFormatException){
         null
    }
}
fun withMathRound(value: Double, places: Int): Double {
    val scale = 10.0.pow(places.toDouble())
    return (value * scale).roundToInt() / scale
}
fun String.fullTrim() = trim().replace("\uFEFF", "")
fun Double.roundTo2DecimalPlaces(scale: Int) =
    BigDecimal(this).setScale(scale, BigDecimal.ROUND_HALF_UP).toPlainString()