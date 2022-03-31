package com.example.appscreentrack.domain.models

import androidx.room.Embedded
import androidx.room.Relation

data class DayWithDayStats(
    @Embedded val day: Day,
    @Relation(
        entity = DayUsageStatsModel::class,
        parentColumn = "date",
        entityColumn = "dayId"
    )
    val dayStatsList: List<DayUsageStatsModel>
)