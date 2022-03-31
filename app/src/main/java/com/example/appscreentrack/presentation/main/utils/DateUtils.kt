package com.example.appscreentrack.presentation.main.utils

import com.example.appscreentrack.presentation.main.calendar.CalendarDateModel
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    private val horizontalDateList: ArrayList<CalendarDateModel> = arrayListOf()

    fun getDaysOfMonth(): ArrayList<CalendarDateModel> {
        horizontalDateList.clear()
        val cal = Calendar.getInstance(TimeZone.getDefault())
        val maxDay: Int = 9
        var currentDay = cal.get(Calendar.DAY_OF_MONTH)
        val df = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        val cd = SimpleDateFormat("EE", Locale.getDefault())
        val md = SimpleDateFormat("MMM", Locale.getDefault())

        df.timeZone = TimeZone.getDefault()

        var curTimeInMills = System.currentTimeMillis()
        for (i in 0..maxDay) {
            horizontalDateList.add(
                CalendarDateModel(
                    cd.format(cal.time).toString(),
                    df.format(cal.time).substring(3, 5),
                    md.format(cal.time).toString(),
                    findNightTime(cal.timeInMillis)
                )
            )
            currentDay -= 1
            curTimeInMills -= 1 * (24 * 60 * 60 * 1000)
            cal.timeInMillis = curTimeInMills
        }
        horizontalDateList.reverse()
        return horizontalDateList
    }

    private fun findNightTime(milis: Long): Long {

        return getEndDateOfDay(milis)
    }

    private fun getEndDateOfDay(timeStamp: Long): Long {
        val cal: Calendar = Calendar.getInstance()
        cal.timeInMillis = timeStamp
        cal.set(Calendar.MILLISECOND, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        println(cal.timeInMillis)
        return cal.timeInMillis
    }
}