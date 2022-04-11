package com.example.appscreentrack.presentation.main.utils

import android.content.Context
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.example.appscreentrack.R
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

object PieCartUtils {
    private fun addColors(context: Context): ArrayList<Int> {
        val colors = arrayListOf<Int>()
        colors.add(ContextCompat.getColor(context, R.color.colorOne))
        colors.add(ContextCompat.getColor(context, R.color.colorTwo))
        colors.add(ContextCompat.getColor(context, R.color.colorThree))
        colors.add(ContextCompat.getColor(context, R.color.colorFour))
        colors.add(ContextCompat.getColor(context, R.color.colorFive))
        return colors
    }

    //Pie Chart
    fun setPieChart(appNames: HashMap<String, Float>, pieChart: PieChart, context: Context) {
        val pieChartEntry = ArrayList<PieEntry>()
        val sortedAppNames =
            appNames.toList().sortedBy { (_, v) -> v }.toMap()

        for (i in sortedAppNames) {
            pieChartEntry.add(PieEntry(i.value, i.key))
        }

        val pieDataSet = PieDataSet(pieChartEntry, "")
        val pieData = PieData(pieDataSet)

        setPropertiesAndLegends(pieDataSet, pieChart, pieData, context)
    }

    private fun setPropertiesAndLegends(
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