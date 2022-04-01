package com.example.appscreentrack.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.appscreentrack.data.Converters
import com.example.appscreentrack.domain.models.Day
import com.example.appscreentrack.domain.models.DayUsageStatsModel

@Database(entities = [Day::class, DayUsageStatsModel::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun statsDao(): StatsDao
}