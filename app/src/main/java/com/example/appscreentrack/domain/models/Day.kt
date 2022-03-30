package com.example.appscreentrack.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.ZonedDateTime

@Entity
data class Day(
    @PrimaryKey val date: ZonedDateTime,
    val lastUpdated: Long
)