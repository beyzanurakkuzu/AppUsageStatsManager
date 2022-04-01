package com.example.appscreentrack.presentation.main.utils

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager
import androidx.window.layout.WindowMetricsCalculator

class ScreenUtils {
    companion object {

        fun getScreenWidth(activity: Activity): Int {
            val windowMetrics = WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(activity)
            val currentBounds = windowMetrics.bounds // E.g. [0 0 1350 1800]
            return currentBounds.width()
        }

        fun dpToPx(context: Context, value: Int): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                value.toFloat(),
                context.resources.displayMetrics
            ).toInt()
        }
    }
}