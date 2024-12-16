package com.android_ai.csc13009.app.domain.repository.repository
import com.android_ai.csc13009.app.domain.repository.model.GrammarSubtopic

interface IGrammarSubtopicRepository {
    suspend fun getSubtopicsByTopicId(topicId: Int): List<GrammarSubtopic>

    suspend fun getAllSubtopics(): List<GrammarSubtopic>
}