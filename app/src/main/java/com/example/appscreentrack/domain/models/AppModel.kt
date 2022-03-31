package com.example.appscreentrack.domain.models

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable

data class AppModel(
    val packageName: String,
    val appName: String,
    var iconDrawable: Drawable?
) {
    companion object {
        fun fromContext(
            context: Context,
            packageName: String
        ): AppModel {
            var appIcon: Drawable? = null
            var appName: String
            try {
                val pm = context.packageManager
                appIcon = pm.getApplicationIcon(packageName)
                val info = pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
                appName = pm.getApplicationLabel(info).toString()
            } catch (e: Exception) {
                appName = "$packageName (uninstalled)"
            }

            return AppModel(
                packageName,
                appName,
                appIcon
            )
        }
    }
}