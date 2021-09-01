import com.prolificinteractive.materialcalendarview.CalendarDay
import java.text.SimpleDateFormat
import java.util.*

fun CalendarDay.getStringDate(): String {
    val stringDay = if (day in 1..9) {
        "0$day"
    } else {
        day.toString()
    }

    val stringMonth = if (month in 1..9) {
        "0$month"
    } else {
        month.toString()
    }
    return "$stringDay-$stringMonth-$year"
}
fun CalendarDay.getStringFullDate(): String {
    val stringDay = if (day in 1..9) {
        "0$day"
    } else {
        day.toString()
    }

    val stringMonth = if (month in 1..9) {
        "0$month"
    } else {
        month.toString()
    }
    return "$stringDay-$stringMonth-$year 00:00:00"
}

fun Int.getTimeTowDigits(): String {
    return if (this in 1..9) {
        "0$this"
    } else {
        toString()
    }
}

fun getCurrentTime24(): String {
    val formatter = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
    val date = Date()
    return formatter.format(date)
}

fun getLastDayOfMonth(year: Int, month: Int): String {
    val calendar = Calendar.getInstance()
    calendar[year, month - 1] = 1
    calendar[Calendar.DATE] = calendar.getActualMaximum(Calendar.DATE)
    val date = calendar.time
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    return dateFormat.format(date)
}