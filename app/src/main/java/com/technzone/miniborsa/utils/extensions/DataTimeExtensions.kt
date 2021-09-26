package com.technzone.miniborsa.utils.extensions

import com.technzone.miniborsa.utils.DateTimeUtil
import com.technzone.miniborsa.utils.DateTimeUtil.DAY_MONTH_NAME_YEAR_DATE_TIME_FORMATTING
import com.technzone.miniborsa.utils.DateTimeUtil.DAY_MONTH_YEAR_DATE_TIME_FORMATTING
import com.technzone.miniborsa.utils.DateTimeUtil.DAY_NAME_MONTH_NAME_DAY_YEAR_DATE_TIME_FORMATTING
import com.technzone.miniborsa.utils.DateTimeUtil.FULL_DATE_AT_TIME_FORMATTING
import com.technzone.miniborsa.utils.DateTimeUtil.FULL_DATE_TIME_FORMATTING
import com.technzone.miniborsa.utils.DateTimeUtil.FULL_DATE_TIME_With_dash_FORMATTING
import com.technzone.miniborsa.utils.DateTimeUtil.MESSAGE_DATE_FORMATE
import com.technzone.miniborsa.utils.DateTimeUtil.MONTH_NAME_DAY_YEAR_DATE_FORMATTING
import com.technzone.miniborsa.utils.DateTimeUtil.MONTH_NAME_DAY_YEAR_DATE_TIME_FORMATTING
import com.technzone.miniborsa.utils.DateTimeUtil.TIME_FORMATTING_HOUR_MIN_24
import com.technzone.miniborsa.utils.DateTimeUtil.YEAR_NUMBER_FORMATTING
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.absoluteValue

fun Long.minToMillisecond(): Long {
    return TimeUnit.MINUTES.toMillis(this)
}

fun Long.secondToMillisecond(): Long {
    return TimeUnit.SECONDS.toMillis(this)
}

fun Long.millisecondToSecond(): Long {
    return TimeUnit.MILLISECONDS.toSeconds(this)
}

fun Long.millisecondToHour(): Long {
    return TimeUnit.MILLISECONDS.toHours(this)
}

fun Long.hourToMinutes(): Long {
    return TimeUnit.HOURS.toMinutes(this)
}

fun Long.hourToSecond(): Long {
    return TimeUnit.HOURS.toSeconds(this)
}

fun Long.hourToMillisecond(): Long {
    return TimeUnit.HOURS.toMillis(this)
}

fun Long.millisecondToMinutes(): Long {
    return TimeUnit.MILLISECONDS.toMinutes(this)
}

fun Long.millisecondToDays(): Long {
    return TimeUnit.MILLISECONDS.toDays(this)
}

fun Long.secondFormatting(pattern: String): String {
    return this.secondToMillisecond().millisecondFormatting(pattern)
}

fun Long.millisecondFormatting(pattern: String = DateTimeUtil.FULL_DATE_TIME_FORMATTING): String {
    val dateFormat = SimpleDateFormat(pattern, Locale.ENGLISH)
    val dateTime = Date(this)
    return dateFormat.format(dateTime)
}

fun String?.toMillieSecconds(format: String = DateTimeUtil.FULL_DATE_TIME_FORMATTING): Long {
    return try {
        val dateFormat = SimpleDateFormat(format, Locale.ENGLISH)
        val date = dateFormat.parse(this)
        return date!!.time
    } catch (e: IllegalArgumentException) {
        0
    } catch (e: ParseException) {
        0
    } catch (e: NullPointerException) {
        0
    }
}

fun toMillieSecconds(date: String): Long {
    return try {
        val dateFormat = SimpleDateFormat(FULL_DATE_TIME_FORMATTING, Locale.ENGLISH)
        val date = dateFormat.parse(date)
        return date!!.time
    } catch (e: IllegalArgumentException) {
        0
    } catch (e: ParseException) {
        0
    }
}

fun getTodayName(): String {
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("EEEE", Locale.ENGLISH)
    return dateFormat.format(calendar.time)
}

fun String.getAgeInYears(): Int {
    val inFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
    inFormat.timeZone = TimeZone.getTimeZone("UTC")

    val outFormat = SimpleDateFormat("yyyy", Locale.ENGLISH)
    outFormat.timeZone = TimeZone.getDefault()
    val year = outFormat.format(inFormat.parse(this))
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    return currentYear - year.toInt()
}

fun String.getAgeInMonths(): Int {
    val inFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
    inFormat.timeZone = TimeZone.getTimeZone("UTC")

    val outFormat = SimpleDateFormat("MM", Locale.ENGLISH)
    outFormat.timeZone = TimeZone.getDefault()
    val month = outFormat.format(inFormat.parse(this))
    val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1
    return when {
        currentMonth == month.toInt() -> {
            1
        }
        currentMonth > month.toInt() -> {
            currentMonth - month.toInt()
        }
        else -> {
            0
        }
    }
}

fun getCurrentTime(): String {
    val formatter = SimpleDateFormat("HH:mm:ss")
    val date = Date()
    return formatter.format(date)
}

fun getCurrentHour(): String {
    val formatter = SimpleDateFormat("HH")
    val date = Date()
    return formatter.format(date)
}

fun getCurrentDate(): String {
    val formatter = SimpleDateFormat(FULL_DATE_TIME_FORMATTING)
    val date = Date()
    return formatter.format(date)
}

fun getCurrentYear(): String {
    val formatter = SimpleDateFormat(YEAR_NUMBER_FORMATTING)
    val date = Date()
    return formatter.format(date)
}

fun getHourAfter(hour: Int): String {
    val dateFormat = SimpleDateFormat("HH:mm:ss")
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.HOUR_OF_DAY, hour)
    return dateFormat.format(calendar.time)
}

fun getDaysAfter(day: Int): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DATE, day)
    return dateFormat.format(calendar.time)
}

fun String.getDate(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    return dateFormat.format(this.toMillieSecconds())
}

fun String.getFullDate(format: String = DateTimeUtil.FULL_DATE_TIME_FORMATTING): String {
    val dateFormat = SimpleDateFormat(format)
    return dateFormat.format(this.toMillieSecconds(format))
}

fun String?.getFullDateWithDash(): String {
    if(this.isNullOrEmpty())
        return ""
    val dateFormat = SimpleDateFormat(FULL_DATE_TIME_With_dash_FORMATTING)
    return dateFormat.format(this.toMillieSecconds(FULL_DATE_TIME_FORMATTING))
}

fun Date.getMessageDate(): String {
    val dateFormat = SimpleDateFormat(MESSAGE_DATE_FORMATE)
    return dateFormat.format(this.time)
}

fun String.getDateAtTime(): String {
    val dateFormat = SimpleDateFormat("d MMM yyyy' at 'hh:mm a")
    return dateFormat.format(this.toMillieSecconds())
}

fun String.getDateWithDayName(): String {
    val dateFormat = SimpleDateFormat("EEEE d MMM yyyy")
    return dateFormat.format(this.toMillieSecconds())
}

fun String?.getDateWithMonthName(): String {
    if(this.isNullOrEmpty())
        return ""
    return try {
        val dateFormat = SimpleDateFormat(MONTH_NAME_DAY_YEAR_DATE_FORMATTING)
        dateFormat.format(this.toMillieSecconds())
    } catch (e: IllegalArgumentException) {
        ""
    }

}
fun String.getDateWithDayMonthNameYear(): String {
    return try {
        val dateFormat = SimpleDateFormat(DAY_MONTH_NAME_YEAR_DATE_TIME_FORMATTING)
        dateFormat.format(this.toMillieSecconds())
    } catch (e: IllegalArgumentException) {
        ""
    }

}

fun String?.getDateWithMonthNameWithTime(): String {
    if(this.isNullOrEmpty())
        return ""
    return try {
        val dateFormat = SimpleDateFormat(MONTH_NAME_DAY_YEAR_DATE_TIME_FORMATTING)
        dateFormat.format(this.toMillieSecconds())
    } catch (e: IllegalArgumentException) {
        ""
    }

}

fun String.getDateWithDayNameMonthName(): String {
    return try {
        val dateFormat = SimpleDateFormat(DAY_NAME_MONTH_NAME_DAY_YEAR_DATE_TIME_FORMATTING)
        dateFormat.format(this.toMillieSecconds())
    } catch (e: IllegalArgumentException) {
        ""
    }

}

fun String.getTime(): String {
    val formatter = SimpleDateFormat(TIME_FORMATTING_HOUR_MIN_24)
    return formatter.format(this.toMillieSecconds())
}


fun String.getDayName(): String {
    val dateFormat = SimpleDateFormat("EEE", Locale.ENGLISH)
    return dateFormat.format(this.toMillieSecconds())
}

fun String.getDayNumber(): String {
    val dateFormat = SimpleDateFormat("dd", Locale.ENGLISH)
    return dateFormat.format(this.toMillieSecconds())
}

fun Long.getHour(): String {
    val dateFormat = SimpleDateFormat("HH", Locale.ENGLISH)
    return dateFormat.format(this)
}
fun Long.getMinute(): String {
    val dateFormat = SimpleDateFormat("mm", Locale.ENGLISH)
    return dateFormat.format(this)
}

fun String.getMonthName(): String {
    val dateFormat = SimpleDateFormat("MMM", Locale.ENGLISH)
    return dateFormat.format(this.toMillieSecconds())
}

fun String?.getDateFormattedForNews(): String {
    val dateFormat = SimpleDateFormat(DAY_MONTH_YEAR_DATE_TIME_FORMATTING, Locale.ENGLISH)
    return dateFormat.format(this.toMillieSecconds())
}

fun Long.getNotificationDateForamteed(): String {
    val dateFormat = SimpleDateFormat("dd-MMM", Locale.ENGLISH)
    return dateFormat.format(this)
}

fun Date.incrementDateByDay(amount: Int): Date {
    val cal = Calendar.getInstance()
    cal.time = this
    cal.add(Calendar.DAY_OF_MONTH, amount) //Adds a day
    return cal.time
}


fun Date.decrementDateByDay(amount: Int): Date {
    val cal = Calendar.getInstance()
    cal.time = this
    cal.add(Calendar.DAY_OF_MONTH, amount.absoluteValue.times(-1)) //Goes to previous day
    return cal.time
}

