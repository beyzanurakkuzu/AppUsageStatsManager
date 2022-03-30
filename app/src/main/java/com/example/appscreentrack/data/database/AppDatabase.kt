package com.example.appscreentrack.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.appscreentrack.domain.models.Day
import com.example.appscreentrack.domain.models.DayStats

@Database(entities = [Day::class, DayStats::class], version = 1, exportSchema = true)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun statsDao(): StatsDao
}