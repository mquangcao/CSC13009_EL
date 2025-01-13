package com.android_ai.csc13009.app.data.local.repository

import com.android_ai.csc13009.app.data.local.dao.GrammarQuestionDao
import com.android_ai.csc13009.app.domain.models.GrammarQuestion
import com.android_ai.csc13009.app.domain.repository.IGrammarQuestionRepository
import com.android_ai.csc13009.app.utils.mapper.toDomain

class GrammarQuestionRepository(private val grammarQuestionDao: GrammarQuestionDao) : IGrammarQuestionRepository {
    override suspend fun getQuestionsByTopicId(topicId: Int): List<GrammarQuestion> {
        return grammarQuestionDao.getQuestionsByTopicId(topicId).map { it.toDomain() }
    }
    override suspend fun getQuestionById(questionId: Int): GrammarQuestion {
        return grammarQuestionDao.getQuestionById(questionId).toDomain()
    }
}