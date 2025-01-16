package com.android_ai.csc13009.app.domain.repository
import com.android_ai.csc13009.app.domain.models.GrammarTopic

interface IGrammarTopicRepository {
    suspend fun getTopicsByLevel(levelId: String): List<GrammarTopic>
    suspend fun getTopicByName(topicName: String): GrammarTopic?
    suspend fun getAllTopics(): List<GrammarTopic>
}