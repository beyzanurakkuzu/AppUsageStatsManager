package com.example.appscreentrack.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.appscreentrack.domain.models.App
import com.example.appscreentrack.domain.models.AppUsage

data class RelationApp (
    @Embedded val lockedAppDao: App,
    @Relation(
        parentColumn = "packageName",
        entityColumn = "packageName"
    )
    val usageApp: List<AppUsage>
)