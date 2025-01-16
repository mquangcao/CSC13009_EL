package com.android_ai.csc13009.app.data.repository

import com.android_ai.csc13009.app.data.remote.repository.FirestoreGrammarTopicRepository
import com.android_ai.csc13009.app.domain.models.GrammarTopic
import com.android_ai.csc13009.app.domain.repository.IGrammarTopicRepository
import com.android_ai.csc13009.app.utils.mapper.toDomain

class GrammarTopicRepository(private val firestoreGrammarTopicRepository: FirestoreGrammarTopicRepository) :
    IGrammarTopicRepository {

    override suspend fun getTopicsByLevel(levelId: String): List<GrammarTopic> {
        val firestoreTopics = firestoreGrammarTopicRepository.getTopicsByLevel(levelId)
        return firestoreTopics.map { it.toDomain() }
    }

    override suspend fun getTopicByName(topicName: String): GrammarTopic? {
        val firestoreTopic = firestoreGrammarTopicRepository.getTopicByName(topicName)
        return firestoreTopic?.toDomain()
    }

    override suspend fun getAllTopics(): List<GrammarTopic> {
        val firestoreTopics = firestoreGrammarTopicRepository.getAllTopics()
        return firestoreTopics.map { it.toDomain() }
    }
}

