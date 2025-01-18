package com.android_ai.csc13009.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "WordSchedule",
    primaryKeys = ["userId", "wordId"]
)
data class WordScheduleEntity(
    val userId: String, // Identifier for the user
    val wordId: Int,    // Links to WordModel
    val lastReviewed: Long,
    val nextReview: Long,
    val reviewCount: Int = 0,
    val successRate: Float = 0.0f
)
