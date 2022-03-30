package com.example.appscreentrack.domain.models

sealed class AppUsageState {
    object Loading : AppUsageState()
    object Error : AppUsageState()
    data class Content(
        val usageList: List<AppUsage>,
        val map : HashMap<String, Float>
    ) : AppUsageState()
}