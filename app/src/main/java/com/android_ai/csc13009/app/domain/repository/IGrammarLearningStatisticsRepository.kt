package com.android_ai.csc13009.app.domain.repository

interface IGrammarLearningStatisticsRepository {
    suspend fun getCorrectAnswerCount(): Int
    suspend fun getTotalAnswerCount():Int
}