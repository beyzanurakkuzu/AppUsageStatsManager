package com.example.appscreentrack.domain.repository

import android.util.Log
import com.example.appscreentrack.presentation.AppUsageManager
import com.example.appscreentrack.data.database.StatsDao
import com.example.appscreentrack.domain.models.UsageListModel
import com.example.appscreentrack.presentation.main.utils.TimeUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import org.threeten.bp.temporal.ChronoUnit
import javax.inject.Inject

class UsageStatsRepository @Inject constructor(
    private val statsDao: StatsDao,
    private val appUsageManager: AppUsageManager
) {

    suspend fun getCurrentDayUsageData(millis: Long): Flow<UsageListModel> {

        val clickedDay = TimeUtils.getZonedDateTime(millis , ChronoUnit.DAYS)
        val oneDayAgo = TimeUtils.getZonedDateTime(millis-24*60*60*1000 , ChronoUnit.DAYS)

        return statsDao.getClickedDayWithUsageStats(clickedDay,oneDayAgo).filterNotNull().map { dayWithStats ->
            UsageListModel(
                appUsageManager.getUsageList(dayWithStats)
            )
        }
    }

    suspend fun updateData() {
        Log.d("UsageStatsRepository", "updating...")
        statsDao.updateDayWithUsageList(appUsageManager.getDayWithStatsForTenDay())
    }

}