package com.android_ai.csc13009.app.domain.repository
import com.android_ai.csc13009.app.domain.models.GrammarSubtopic

interface IGrammarSubtopicRepository {
    suspend fun getSubtopicsByTopicId(topicId: String): List<GrammarSubtopic>
    suspend fun getAllSubtopics(): List<GrammarSubtopic>
}