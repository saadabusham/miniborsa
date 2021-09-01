package com.technzone.miniborsa.utils

import com.technzone.miniborsa.utils.extensions.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue


object DateTimeUtil {

    const val FULL_DATE_TIME_FORMATTING = "dd-MM-yyyy HH:mm:ss"
    const val FULL_DATE_AT_TIME_FORMATTING = "dd MMM yyyy | HH:mm"
    const val FULL_DATE_TIME_With_dash_FORMATTING = "dd - MM - yyyy | HH:mm"
    const val MESSAGE_DATE_FORMATE = "MMM/dd HH:mm"
    const val YEAR_MONTH_DAY_DATE_TIME_FORMATTING = "yyyy-MM-dd"
    const val DATE_TIME_FORMATTING = "dd/mm/yyyy hh:mm a"
    const val DAY_MONTH_YEAR_DATE_TIME_FORMATTING = "dd /mm/ yyyy"
    const val DAY_MONTH_NAME_YEAR_DATE_TIME_FORMATTING = "dd MMM yyyy"
    const val MONTH_NAME_DAY_YEAR_DATE_FORMATTING = "MMM d , yyyy"
    const val MONTH_NAME_DAY_YEAR_DATE_TIME_FORMATTING = "MMM d , yyyy | HH:mm"
    const val DAY_NAME_MONTH_NAME_DAY_YEAR_DATE_TIME_FORMATTING = "EEE, d MMM yyyy - HH:mm"
    const val YEAR_NUMBER_FORMATTING = "yyyy"
    const val DAY_NUMBER_FORMATTING = "dd"
    const val HOUR_IN_FORMATTING_value = "KK"

    const val SHORT_DAY_NAME_FORMATTING = "EEE"
    const val FULL_DAY_NAME_FORMATTING = "EEEE"
    const val MONTH_NUMBER_FORMATTING = "MM"
    const val SHORT_MONTH_NAME_FORMATTING = "MMM"
    const val FULL_MONTH_NAME_FORMATTING = "MMMM"

    const val TIME_FORMATTING_MIN_AND_SECOND = "(ss)"
    const val TIME_FORMATTING_MIN = "mm"
    const val TIME_FORMATTING_HOUR_MIN = "hh:mma"
    const val TIME_FORMATTING_AM_PM = "a"
    const val TIME_FORMATTING_HOUR_MIN_24 = "HH:mm"
    const val TIME_FORMATTING_HOUR_24 = "HH"
    const val TIME_FORMATTING_HOUR_12 = "hh"

    const val IHospital_TEXT_AND_NUMBERS_FORMAT = "EEE dd MMMM yyyy"

    fun getStartOfDayInMillis(date: Date? = null): Long {
        val calendar: Calendar = Calendar.getInstance(Locale.ENGLISH)
        if (date != null)
            calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    fun getStartOfMonthInMillis(month: Int? = null): Long {
        val calendar: Calendar = Calendar.getInstance()
        if (month != null) {
            calendar.set(Calendar.MONTH, month)
        }
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH))
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY))
        calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE))
        calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND))
        calendar.set(Calendar.MILLISECOND, calendar.getActualMinimum(Calendar.MILLISECOND))

        return calendar.timeInMillis
    }

    fun getEndOfMonthInMillis(month: Int? = null): Long {
        val calendar: Calendar = Calendar.getInstance()
        if (month != null) {
            calendar.set(Calendar.MONTH, month)
        }
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY))
        calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE))
        calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND))
        calendar.set(Calendar.MILLISECOND, calendar.getActualMaximum(Calendar.MILLISECOND))
        return calendar.timeInMillis
    }


    fun getStartOfYearMonthInMillis(year: Int? = null, month: Int? = null): Long {
        val calendar: Calendar = Calendar.getInstance()

        if (year != null) {
            calendar.set(Calendar.YEAR, year)
        }
        if (month != null) {
            calendar.set(Calendar.MONTH, month)
        }
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH))
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY))
        calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE))
        calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND))
        calendar.set(Calendar.MILLISECOND, calendar.getActualMinimum(Calendar.MILLISECOND))

        return calendar.timeInMillis
    }

    fun getEndOfYearMonthInMillis(year: Int? = null, month: Int? = null): Long {
        val calendar: Calendar = Calendar.getInstance()
        if (year != null) {
            calendar.set(Calendar.YEAR, year)
        }
        if (month != null) {
            calendar.set(Calendar.MONTH, month)
        }
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY))
        calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE))
        calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND))
        calendar.set(Calendar.MILLISECOND, calendar.getActualMaximum(Calendar.MILLISECOND))
        return calendar.timeInMillis
    }

    fun getDateInMillis(year: Int, month: Int, day: Int): Long {
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        return calendar.timeInMillis
    }

    fun getFullDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat(FULL_DATE_TIME_FORMATTING, Locale.ENGLISH)
        return dateFormat.format(calendar.time)
    }

    fun changeDateTime(
        date: Date,
        hourOfDay: Int = 0,
        minute: Int = 0,
        second: Int = 0,
        millisecond: Int = 0
    ): Long {
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, second)
        calendar.set(Calendar.MILLISECOND, millisecond)
        return calendar.timeInMillis
    }

    fun changeDateFormat(
        date: String,
        inputFormat: String = FULL_DATE_TIME_FORMATTING,
        outputFormat: String = YEAR_MONTH_DAY_DATE_TIME_FORMATTING,
        handleLocalization: Boolean = false
    ): String {
        val input = SimpleDateFormat(
            inputFormat,
            if (handleLocalization) Locale.getDefault() else Locale.ENGLISH
        )
        val output = SimpleDateFormat(
            outputFormat,
            if (handleLocalization) Locale.getDefault() else Locale.ENGLISH
        )

        try {
            val inputDate = input.parse(date) ?: return "" // parse input
            return output.format(inputDate)
        } catch (e: Exception) {
            return date
        }
    }

    fun incrementDateByHour(date: Date, amount: Int): Date {
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.HOUR_OF_DAY, amount) //Adds a day
        return cal.time
    }

    fun incrementDateByMinute(date: Date, amount: Int): Date {
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.MINUTE, amount) //Adds a day
        return cal.time
    }

    fun incrementDateByDay(date: Date, amount: Int): Date {
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.DAY_OF_MONTH, amount) //Adds a day
        return cal.time
    }


    fun decrementDateByDay(date: Date, amount: Int): Date {
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.DAY_OF_MONTH, amount.absoluteValue.times(-1)) //Goes to previous day
        return cal.time
    }

    fun incrementDateByMonth(date: Date, amount: Int): Date {
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.MONTH, amount) //Adds a day
        return cal.time
    }


    fun decrementDateByMonth(date: Date, amount: Int): Date {
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.MONTH, amount.absoluteValue.times(-1)) //Goes to previous day
        return cal.time
    }

    fun incrementDateByYear(date: Date, amount: Int): Date {
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.YEAR, amount) //Adds a day
        return cal.time
    }


    fun decrementDateByYear(date: Date, amount: Int): Date {
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.YEAR, amount.absoluteValue.times(-1)) //Goes to previous day
        return cal.time
    }

    fun getTimeStampFromStringDate(
        inputDate: String,
        inputFormat: String = FULL_DATE_TIME_FORMATTING
    ): Long {
        val formatter: DateFormat = SimpleDateFormat(inputFormat, Locale.ENGLISH)
        val date = formatter.parse(inputDate) as Date
        return date.time
    }

    fun calculateAge(dateOfAge: String?, inputFormat: String = FULL_DATE_TIME_FORMATTING): String =
        (System.currentTimeMillis() - (dateOfAge.toDate(inputFormat)?.time
            ?: 0)).millisecondFormatting(
            YEAR_NUMBER_FORMATTING
        )


    fun getDiffInDays(day1: Long, day2: Long): Int {
        val diff = day1 - day2
        return diff.millisecondToDays().toInt()
    }

    fun getDiffInHours(day1: Long, day2: Long): Int {
        val diff = day1 - day2
        return diff.millisecondToHour().toInt()
    }

    fun getDiffInMints(day1: Long, day2: Long): Int {
        val diff = day1 - day2
        return diff.millisecondToMinutes().toInt()
    }
}

