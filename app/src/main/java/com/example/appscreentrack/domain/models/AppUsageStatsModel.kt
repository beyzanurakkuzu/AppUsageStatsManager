package com.example.appscreentrack.domain.models

data class AppUsageStatsModel(
    val app: AppModel,
    var totalTime: Long,
    var lastUsed: Long,
    val oneDayAgoTime: Long = 0,
    val differenceBetweenOneDayAgoTime: Long = 0L
)
