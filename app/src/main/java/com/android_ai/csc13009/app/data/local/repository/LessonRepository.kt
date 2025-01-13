package com.android_ai.csc13009.app.data.local.repository

import com.android_ai.csc13009.app.data.local.dao.LessonDao
import com.android_ai.csc13009.app.domain.models.Lesson
import com.android_ai.csc13009.app.domain.repository.repository.ILessonRepository
import com.android_ai.csc13009.app.utils.mapper.toDomain

class LessonRepository(private val lessonDao: LessonDao, private val questionRepository: QuestionRepository) : ILessonRepository {
    override suspend fun getLessonsByChapterId(chapterId: Int): List<Lesson> {
        val result = lessonDao.getLessonsByChapterId(chapterId)
        return result.map { lesson ->
            val questions = questionRepository.getQuestionsByLessonId(lesson.id)
            lesson.toDomain(questions)
        }
    }

}