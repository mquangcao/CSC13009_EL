package com.android_ai.csc13009.app.domain.repository.repository

import com.android_ai.csc13009.app.domain.models.Question

interface IQuestionRepository {
    suspend fun getQuestionsByLessonId(lessonId: Int): List<Question>

}