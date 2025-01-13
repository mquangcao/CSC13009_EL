package com.android_ai.csc13009.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Answers")
data class AnswersEntity(
    @PrimaryKey
    val id: Int,
    val questionId: Int,
    val answerWordId: Int?,
    val text : String,
    val isCorrect: Boolean
)
