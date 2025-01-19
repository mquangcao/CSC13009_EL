package com.android_ai.csc13009.app.domain.repository

import com.android_ai.csc13009.app.domain.models.ListeningLesson

interface IListeningLessonRepository {
    suspend fun getLessonsByTopicId(topicId: String): List<ListeningLesson>
}