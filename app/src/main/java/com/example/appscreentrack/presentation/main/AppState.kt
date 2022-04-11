package com.example.appscreentrack.presentation.main

import com.example.appscreentrack.domain.models.AppUsageStatsModel

sealed class AppState {
    object Loading : AppState()
    object Error : AppState()
    data class Content(
        val usageList: List<AppUsageStatsModel>,
        val map: HashMap<String, Float>
    ) : AppState()
}