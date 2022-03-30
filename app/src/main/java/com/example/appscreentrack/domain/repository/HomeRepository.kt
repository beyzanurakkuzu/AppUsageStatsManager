package com.example.appscreentrack.domain.repository

import android.util.Log
import com.example.appscreentrack.presentation.AppUsageManager
import com.example.appscreentrack.data.database.StatsDao
import com.example.appscreentrack.domain.models.UsageData
import com.example.appscreentrack.presentation.main.utils.Utils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import org.threeten.bp.temporal.ChronoUnit
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val statsDao: StatsDao,
    private val appUsageManager: AppUsageManager
) {

    suspend fun getCurrentDayUsageData(millis: Long): Flow<UsageData> {

        val currentDay = Utils.getZonedDateTime(millis , ChronoUnit.DAYS)
        val yesterday = Utils.getZonedDateTime(millis-24*60*60*1000 , ChronoUnit.DAYS)

        return statsDao.getDayWithDayStats(currentDay,yesterday).filterNotNull().map { dayWithStats ->
            UsageData(
                appUsageManager.getUsageList(dayWithStats)
            )
        }
    }

    suspend fun updateData() {
        Log.d("HomeRepository", "updating...")
        statsDao.upsert(appUsageManager.getDayWithStatsForWeek())
    }

}