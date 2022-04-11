
package com.example.appscreentrack.data.database

import androidx.room.*
import com.example.appscreentrack.domain.models.Day
import com.example.appscreentrack.domain.models.DayUsageStatsModel
import com.example.appscreentrack.domain.models.DayWithDayStats
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.ZonedDateTime

@Dao
abstract class StatsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertDay(day: Day): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertDayStats(dayStats: DayUsageStatsModel): Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun updateDay(day: Day)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun updateDayStats(dayStats: DayUsageStatsModel)

    @Transaction
    open suspend fun updateDayWithUsageList(dayWithDayStats: List<DayWithDayStats>) {
        dayWithDayStats.forEach {
            if (insertDay(it.day) == (-1).toLong())
                updateDay(it.day)


            it.dayStatsList.forEach { stats ->
                if (insertDayStats(stats) == (-1).toLong())
                    updateDayStats(stats)

            }
        }
    }

    @Transaction
    @Query("SELECT * FROM Day WHERE date IS :date OR date IS :yesterday ORDER BY date desc")
    abstract fun getClickedDayWithUsageStats(
        date: ZonedDateTime,
        yesterday: ZonedDateTime
    ): Flow<List<DayWithDayStats>>
}