package com.android_ai.csc13009.app.domain.models

import java.sql.Date

data class GrammarLearningStatistics (
    val id: Int,
    val grammarQuestionId: Int,
    val userId: Int,
    val isCorrect: Boolean,
    val date: Date
)