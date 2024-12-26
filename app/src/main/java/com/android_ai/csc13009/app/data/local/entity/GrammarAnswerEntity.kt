package com.android_ai.csc13009.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "GrammarAnswer")
data class GrammarAnswerEntity(
    @PrimaryKey val grammarAnswerId: Int,
    val grammarQuestionId: Int,
    val answer: String,
    val isCorrect: Boolean
)
