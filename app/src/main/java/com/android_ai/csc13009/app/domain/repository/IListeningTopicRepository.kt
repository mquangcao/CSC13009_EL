package com.android_ai.csc13009.app.domain.repository

import com.android_ai.csc13009.app.domain.models.ListeningTopic
import com.android_ai.csc13009.app.domain.models.ListeningTopics

interface IListeningTopicRepository {
    suspend fun getTopicList(): List<ListeningTopics>
    suspend fun getTopicDetails(topicId: String): ListeningTopic
}