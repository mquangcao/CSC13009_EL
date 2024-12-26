package com.android_ai.csc13009.app.data.local.repository

import com.android_ai.csc13009.app.data.local.dao.GrammarLearningStatisticsDao
import com.android_ai.csc13009.app.domain.repository.IGrammarLearningStatisticsRepository


class GrammarLearningStatisticsRepository(private val grammarLearningStatisticsDao: GrammarLearningStatisticsDao) :
    IGrammarLearningStatisticsRepository {
    override suspend fun getCorrectAnswerCount(): Int {
        return grammarLearningStatisticsDao.getCorrectAnswerCount()
    }
    override suspend fun getTotalAnswerCount(): Int {
        return grammarLearningStatisticsDao.getTotalAnswerCount()

    }
}