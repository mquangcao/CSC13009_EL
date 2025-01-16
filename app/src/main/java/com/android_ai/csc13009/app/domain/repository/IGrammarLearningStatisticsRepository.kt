package com.android_ai.csc13009.app.domain.repository

import com.android_ai.csc13009.app.domain.models.GrammarLearningStatistics

interface IGrammarLearningStatisticsRepository {
    suspend fun getCorrectAnswerCount(): Int
    suspend fun getTotalAnswerCount():Int
    suspend fun recordQuestion(): GrammarLearningStatistics
}