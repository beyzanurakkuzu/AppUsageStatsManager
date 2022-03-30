package com.example.appscreentrack.data.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.example.appscreentrack.domain.models.Day
import com.example.appscreentrack.domain.models.DayStats
import com.example.appscreentrack.domain.models.DayWithDayStats
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.ZonedDateTime

@Dao
abstract class StatsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(day: Day): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(dayStats: DayStats): Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun update(day: Day)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun update(dayStats: DayStats)

    @Transaction
    open suspend fun upsert(dayWithDayStats: List<DayWithDayStats>) {
        dayWithDayStats.forEach {
            if (insert(it.day) == (-1).toLong()) {
                update(it.day)
            }

            it.dayStats.forEach { stats ->
                if (insert(stats) == (-1).toLong()) {
                    update(stats)
                }
            }
        }
    }

    @Delete
    abstract suspend fun delete(day: Day)

    @Delete
    abstract suspend fun delete(vararg dayStats: DayStats)

    @Transaction
    @Query("SELECT * FROM Day")
    abstract fun getDaysWithDayStats(): Flow<List<DayWithDayStats>?>

    @Transaction
    @Query("SELECT * FROM Day WHERE date IS :date OR date IS :yesterdate ORDER BY date desc")
    abstract fun getDayWithDayStats(
        date: ZonedDateTime,
        yesterdate: ZonedDateTime
    ): Flow<List<DayWithDayStats>>

    @Query("SELECT * FROM DayStats WHERE packageName IS :packageName")
    abstract fun getDayStats(packageName: String): Flow<List<DayStats>>

    @Transaction
    @Query("SELECT * FROM Day WHERE date IS :date")
    abstract fun getCurrentDayWithDayStats(date: ZonedDateTime): LiveData<DayWithDayStats?>

    /* @Query("SELECT today-lastday FROM DayStats WHERE timestamp BETWEEN :today AND :lastDay group by packageName ")
     abstract fun newAllExpensesFromTo(today: Long?, lastDay: Long?): Int

     @Query("SELECT * FROM DayStats WHERE timestamp BETWEEN :currentTime AND :currentTime-86400000 GROUP BY packageName")
     abstract fun getYesterdayDay(currentTime: ZonedDateTime): MutableLiveData<DayWithDayStats>*/
}