package com.example.appscreentrack.domain.models

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable

data class App(
    val packageName: String,
    val appName: String,
    var iconDrawable: Drawable?
) {
    companion object {
        fun fromContext(
            context: Context,
            packageName: String
        ): App {
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

            return App(
                packageName,
                appName,
                appIcon
            )
        }
    }
}