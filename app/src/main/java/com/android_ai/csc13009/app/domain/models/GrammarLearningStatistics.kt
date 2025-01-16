package com.android_ai.csc13009.app.domain.models

import java.sql.Date

data class GrammarLearningStatistics (
    val id: String,
    val grammarQuestionId: String,
    val userId: String,
    val isCorrect: Boolean,
    val date: Date
)