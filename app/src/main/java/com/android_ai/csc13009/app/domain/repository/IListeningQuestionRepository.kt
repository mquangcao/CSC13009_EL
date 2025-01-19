package com.android_ai.csc13009.app.domain.repository

import com.android_ai.csc13009.app.domain.models.ListeningQuestion

interface IListeningQuestionRepository {
    suspend fun getQuestionsByLessonId(lessonId: String): List<ListeningQuestion>

}