package com.android_ai.csc13009.app.data.local.repository

import com.android_ai.csc13009.app.data.local.dao.GrammarSubtopicDao
import com.android_ai.csc13009.app.domain.repository.model.GrammarSubtopic
import com.android_ai.csc13009.app.domain.repository.repository.IGrammarSubtopicRepository
import com.android_ai.csc13009.app.utils.mapper.toDomain

class GrammarSubtopicRepository(private val grammarSubtopicDao: GrammarSubtopicDao): IGrammarSubtopicRepository {
    override suspend fun getSubtopicsByTopicId(topicId: Int): List<GrammarSubtopic> {
        return grammarSubtopicDao.getSubtopicsByTopicId(topicId).map { it.toDomain() }
    }

    override suspend fun getAllSubtopics(): List<GrammarSubtopic> {
        return grammarSubtopicDao.getAllSubtopics().map { it.toDomain() }
    }

}