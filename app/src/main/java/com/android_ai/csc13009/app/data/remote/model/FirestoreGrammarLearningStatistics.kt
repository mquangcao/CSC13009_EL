package com.android_ai.csc13009.app.data.remote.model

import java.util.Date

data class FirestoreGrammarLearningStatistics(
    val id: String,
    val grammarQuestionId: String,
    val userId: String,
    val isCorrect: Boolean,
    val date: Date
)