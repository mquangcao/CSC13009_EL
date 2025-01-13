package com.android_ai.csc13009.app.data.repository

import com.android_ai.csc13009.app.data.local.dao.GrammarTopicDao
import com.android_ai.csc13009.app.domain.models.GrammarTopic
import com.android_ai.csc13009.app.domain.repository.IGrammarTopicRepository
import com.android_ai.csc13009.app.utils.mapper.toDomain

class GrammarTopicRepository(private val grammarTopicDao: GrammarTopicDao,):
    IGrammarTopicRepository {
    override suspend fun getTopicsByLevel(levelId: Int): List<GrammarTopic> {
        return grammarTopicDao.getTopicsByLevel(levelId).map { it.toDomain() }
    }

    override suspend fun getTopicByName(topicName: String): GrammarTopic? {
        return grammarTopicDao.getTopicByName(topicName)?.toDomain()
    }

    override suspend fun getAllTopics(): List<GrammarTopic> {
        return grammarTopicDao.getAllTopics().map { it.toDomain() }
    }
}