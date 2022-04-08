package com.example.appscreentrack.domain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import org.threeten.bp.ZonedDateTime

@Entity(
    primaryKeys = ["packageName", "dayId"],
    foreignKeys = [
        ForeignKey(
            entity = Day::class,
            parentColumns = ["date"],
            childColumns = ["dayId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DayUsageStatsModel(
    val packageName: String,
    val totalTime: Long,
    val lastUsed: Long,
    val timeStamp: Long,
    val yesterdayData: Long,
    @ColumnInfo(index = true)
    val dayId: ZonedDateTime
)