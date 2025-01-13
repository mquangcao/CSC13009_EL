package com.android_ai.csc13009.app.domain.repository.repository

import com.android_ai.csc13009.app.domain.models.Lesson

interface ILessonRepository {
    suspend fun getLessonsByChapterId(chapterId: Int): List<Lesson>

}