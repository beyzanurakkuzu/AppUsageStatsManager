package com.example.appscreentrack.presentation.main.utils

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.appscreentrack.R
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import org.threeten.bp.Duration
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.temporal.ChronoUnit

object Utils {

    fun getZonedDateTime(millis: Long, truncatedTo: ChronoUnit): ZonedDateTime =
        Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).truncatedTo(truncatedTo)

    fun getUsageTimeString(l: Long): String {
        val millis = if (l < 0) (-1L) * l else l
        var timeLeft = Duration.ofMillis(millis)
        val hours = timeLeft.toHours()

        timeLeft = timeLeft.minusHours(hours)
        val minutes = timeLeft.toMinutes()

        timeLeft = timeLeft.minusMinutes(minutes)
        val seconds = timeLeft.seconds

        return when {
            hours >= 1 -> {
                String.format("%dh %dm %ds", hours, minutes, seconds)
            }
            minutes >= 1 -> {
                String.format("%dm %ds", minutes, seconds)
            }
            else -> {
                // assuming all apps are used for at least >= 1 second
                String.format("%ds", seconds)
            }
        }
    }

    private fun addColors(context: Context): ArrayList<Int> {
        val colors = arrayListOf<Int>()
        colors.add(ContextCompat.getColor(context, R.color.colorOne))
        colors.add(ContextCompat.getColor(context, R.color.colorTwo))
        colors.add(ContextCompat.getColor(context, R.color.colorThree))
        colors.add(ContextCompat.getColor(context, R.color.colorFour))
        colors.add(ContextCompat.getColor(context, R.color.colorFive))
        return colors
    }

    fun setPropertiesAndLegends(
        pieDataSet: PieDataSet,
        pieChart: PieChart,
        pieData: PieData,
        context: Context
    ) {
        pieDataSet.colors = addColors(context)
        pieChart.description = null
        pieChart.isDrawHoleEnabled = false
        pieChart.setTouchEnabled(false)
        pieChart.setUsePercentValues(true)
        pieChart.setDrawEntryLabels(false)

        val legend = pieChart.legend
        legend.verticalAlignment = Legend.LegendVerticalAlignment.CENTER
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        legend.orientation = Legend.LegendOrientation.VERTICAL

        pieData.setDrawValues(false)
        pieData.notifyDataChanged()
        pieChart.data = pieData
        pieChart.notifyDataSetChanged()
        pieChart.invalidate()
    }
}