package com.android_ai.csc13009.app.data.repository

import com.android_ai.csc13009.app.data.remote.repository.FirestoreGrammarSubtopicRepository
import com.android_ai.csc13009.app.domain.models.GrammarSubtopic
import com.android_ai.csc13009.app.domain.repository.IGrammarSubtopicRepository
import com.android_ai.csc13009.app.utils.mapper.toDomain

class GrammarSubtopicRepository(private val firestoreGrammarSubtopicRepository: FirestoreGrammarSubtopicRepository) :
    IGrammarSubtopicRepository {

    override suspend fun getSubtopicsByTopicId(topicId: String): List<GrammarSubtopic> {
        val firestoreSubtopics = firestoreGrammarSubtopicRepository.getSubtopicsByTopicId(topicId)
        return firestoreSubtopics.map { it.toDomain() }
    }

    override suspend fun getAllSubtopics(): List<GrammarSubtopic> {
        val firestoreSubtopics = firestoreGrammarSubtopicRepository.getAllSubtopics()
        return firestoreSubtopics.map { it.toDomain() }
    }
}
