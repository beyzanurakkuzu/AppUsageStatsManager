package com.example.appscreentrack.presentation.main.utils

import org.threeten.bp.Duration
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.temporal.ChronoUnit

object TimeUtils {

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
}