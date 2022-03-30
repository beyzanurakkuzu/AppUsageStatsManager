package com.example.appscreentrack.presentation.main.calendar

import androidx.room.Entity

@Entity
data class CalendarDateModel(
    val dayOfWeek: String,
    val dayOfMonth: String,
    val month: String,
    val timeStamp: Long
)