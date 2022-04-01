package com.example.appscreentrack.presentation

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import com.example.appscreentrack.domain.models.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.temporal.ChronoUnit
import javax.inject.Inject

class AppUsageManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val usageStatsManager =
        context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager?
    private val now = ZonedDateTime.now(ZoneId.systemDefault())
    private val nowLocalDate = now.toLocalDate()
    private val appsWithDayStats = arrayListOf<DayWithDayStats>()

    suspend fun getUsageList(dayWithStats: List<DayWithDayStats>): List<AppUsageStatsModel> {
        return withContext(Dispatchers.Default) {
            val usageList = arrayListOf<AppUsageStatsModel>()
            //today and OneDayAgo
            if (dayWithStats.size == 1) {
                val today = dayWithStats[0]
                today.dayStatsList.forEach {
                    val app = AppModel.fromContext(context, it.packageName)
                    usageList.add(
                        AppUsageStatsModel(
                            app,
                            it.totalTime,
                            it.lastUsed
                        )
                    )
                }
            } else if (dayWithStats.size == 2) {

                val today = dayWithStats[0]
                val yesterday = dayWithStats[1]

                today.dayStatsList.forEach { daystats ->
                    val app = AppModel.fromContext(context, daystats.packageName)
                    val yd = yesterday.dayStatsList.filter { it.packageName == app.packageName }
                        .sumOf { it.totalTime }
                    val differenceBetweenOneDayAgoTime = daystats.totalTime - yd
                    println("AppModel: ${app.appName} Day : ${today.day.date}  TodayTime: ${daystats.totalTime} YesterdayTime $yd")
                    usageList.add(
                        AppUsageStatsModel(
                            app,
                            daystats.totalTime,
                            daystats.lastUsed,
                            yd,
                            differenceBetweenOneDayAgoTime
                        )
                    )
                }
            }
            return@withContext usageList.sortedByDescending { it.totalTime }
        }
    }

    // 0..9 Day AppUsage
    suspend fun getDayWithStatsForTenDay(): List<DayWithDayStats> {
        return withContext(Dispatchers.IO) {
            for (i in 0..9) {
                val date =
                    nowLocalDate.minusDays(i.toLong()).atStartOfDay(ZoneId.systemDefault())
                appsWithDayStats.add(getDayWithStats(date))
            }
            return@withContext appsWithDayStats
        }
    }

    private fun getDayWithStats(
        date: ZonedDateTime = ZonedDateTime.now(ZoneId.systemDefault())
    ): DayWithDayStats {

        val statsList = ArrayList<DayUsageStatsModel>()
        val utc = ZoneId.of("UTC")
        val defaultZone = ZoneId.systemDefault()
        val startDate = date.toLocalDate().atStartOfDay(defaultZone).withZoneSameInstant(utc)
        val timeStartMillis = startDate.toInstant().toEpochMilli()
        val todayDate = ZonedDateTime.now(ZoneId.systemDefault()).truncatedTo(ChronoUnit.DAYS)

        val timeEndMillis = if (date.truncatedTo(ChronoUnit.DAYS).isEqual(todayDate)) {
            System.currentTimeMillis()
        } else startDate.plusDays(1).minusSeconds(1).toInstant().toEpochMilli()

        if (usageStatsManager != null) {
            val eventsMap = mutableMapOf<String, MutableList<UsageEvents.Event>>()
            val events = usageStatsManager.queryEvents(timeStartMillis, timeEndMillis)

            while (events.hasNextEvent()) {
                val event = UsageEvents.Event()
                events.getNextEvent(event)
                val packageName = event.packageName

                (eventsMap[packageName] ?: mutableListOf()).let {
                    it.add(event)
                    eventsMap[packageName] = it
                }
            }

            eventsMap.forEach { (packageName, events) ->
                val pm = context.packageManager

                if (pm.getLaunchIntentForPackage(packageName) != null) {
                    var startTime=0L
                    var endTime = 0L
                    var totalTime = 0L
                    var lastUsed = 0L
                    var isInitialized = false
                    events.forEach { event ->
                        when (event.eventType) {
                            UsageEvents.Event.ACTIVITY_RESUMED -> { // same as MOVE_TO_FOREGROUND
                                // start time
                                isInitialized = true
                                startTime = event.timeStamp
                                endTime = 0L
                            }

                            UsageEvents.Event.ACTIVITY_PAUSED -> { // same as MOVE_TO_BACKGROUND
                                // end time
                                if (startTime == 0L) {
                                    if (!isInitialized) {
                                        startTime = timeStartMillis
                                        endTime = event.timeStamp
                                        lastUsed = endTime
                                    }
                                } else {
                                    endTime = event.timeStamp
                                    lastUsed = endTime
                                }
                            }
                        }

                        // If both start and end times exist, add the time to totalTime
                        // and reset start and end times
                        if (startTime != 0L && endTime != 0L) {
                            totalTime += endTime - startTime
                            startTime = 0L; endTime = 0L
                        }
                    }

                    // If the end time was not found, it's likely that the app is still running
                    // so assume the end time to be now
                    if (startTime != 0L && endTime == 0L) {
                        lastUsed = timeEndMillis
                        totalTime += lastUsed - startTime
                    }

                    // If total time is more than 1 second
                    if (totalTime >= 1000) {
                        val stats = DayUsageStatsModel(
                            packageName,
                            totalTime,
                            lastUsed, timeStamp = 0L,
                            yesterdayData = 0L,
                            date
                        )
                        statsList.add(stats)
                    }
                }
            }
        }

        return DayWithDayStats(
            Day(
                date, System.currentTimeMillis()
            ),
            statsList
        )
    }

}