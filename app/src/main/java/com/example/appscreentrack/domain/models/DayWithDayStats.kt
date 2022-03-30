package com.example.appscreentrack.domain.models

import androidx.room.Embedded
import androidx.room.Relation

data class DayWithDayStats(
    @Embedded val day: Day,
    @Relation(
        entity = DayStats::class,
        parentColumn = "date",
        entityColumn = "dayId"
    )
    val dayStats: List<DayStats>
)