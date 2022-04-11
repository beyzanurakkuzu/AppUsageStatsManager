package com.example.appscreentrack.presentation.main.utils

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager
import androidx.recyclerview.widget.RecyclerView
import androidx.window.layout.WindowMetricsCalculator

class ScreenUtils {
    companion object {
        private fun getScreenWidth(activity: Activity): Int {
            val windowMetrics =
                WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(activity)
            val currentBounds = windowMetrics.bounds // E.g. [0 0 1350 1800]
            return currentBounds.width()
        }

        private fun dpToPx(context: Context, value: Int): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                value.toFloat(),
                context.resources.displayMetrics
            ).toInt()
        }

        fun setPadding(activity: Activity, context: Context, recyclerView: RecyclerView) {
            val padding: Int = getScreenWidth(activity) / 2 - ScreenUtils.dpToPx(
                context,
                40
            )
            recyclerView.setPadding(padding, 0, padding, 0)
        }
    }
}