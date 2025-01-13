package com.android_ai.csc13009.app.utils.mapper

import com.android_ai.csc13009.app.data.local.entity.LessonEntity
import com.android_ai.csc13009.app.domain.models.Lesson
import com.android_ai.csc13009.app.domain.models.Question

fun LessonEntity.toDomain(questions: List<Question>): Lesson {
    val questionSuccess = questions.count { it.isCorrect == true }
    val totalQuestion = questions.size

    return Lesson(
        id = this.id,
        lessonName = this.lessonName,
        totalQuestion = totalQuestion,
        questionSuccess = questionSuccess,
        questions = questions
    )

}