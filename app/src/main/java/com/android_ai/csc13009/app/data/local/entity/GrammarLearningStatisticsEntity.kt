package com.android_ai.csc13009.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "GrammarLeanringStatistics")
data class GrammarLearningStatisticsEntity(
    @PrimaryKey val id: Int,
    val grammarQuestionId: Int,
    val userId: Int,
    val isCorrect: Boolean,
    val date: Date
)