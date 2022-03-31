package com.example.appscreentrack.data

import androidx.room.TypeConverter
import com.example.appscreentrack.presentation.main.utils.Utils
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.temporal.ChronoUnit

class Converters {
    @TypeConverter
    fun fromDate(date: ZonedDateTime): Long {
        return date.truncatedTo(ChronoUnit.DAYS).toInstant().toEpochMilli()
    }
    @TypeConverter
    fun longToDate(millis: Long): ZonedDateTime {
        return Utils.getZonedDateTime(millis, ChronoUnit.DAYS)
    }
}