package com.example.appscreentrack.data

import androidx.room.TypeConverter
import com.example.appscreentrack.presentation.main.utils.TimeUtils
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.temporal.ChronoUnit

class Converters {
    @TypeConverter
    fun fromDate(date: ZonedDateTime): Long {
        return date.truncatedTo(ChronoUnit.DAYS).toInstant().toEpochMilli()
    }

    @TypeConverter
    fun longToDate(millis: Long): ZonedDateTime {
        return TimeUtils.getZonedDateTime(millis, ChronoUnit.DAYS)
    }
}