package com.android_ai.csc13009.app.utils.mapper

import com.android_ai.csc13009.app.data.local.entity.LessonEntity
import com.android_ai.csc13009.app.data.remote.model.FirestoreLesson
import com.android_ai.csc13009.app.domain.models.Lesson
import com.android_ai.csc13009.app.domain.models.Question

fun LessonEntity.toDomain(questions: List<Question>): Lesson {
    val questionSuccess = 1 //questions.count { it.isCorrect == true }
    val totalQuestion = questions.size

    return Lesson(
        id = TODO(),
        lessonName = this.lessonName,
        totalQuestion = totalQuestion,
        questionSuccess = questionSuccess,
        order = this.order,
        questions = questions,
        isOpen = true,
        isOpenByProgress = true
    )
}

object LessonMapper {
    fun fromFirestore(id: String, data: Map<String, Any>): FirestoreLesson {
        return FirestoreLesson(
            id = id,
            lessonName = data["lessonName"] as? String ?: "",
            topicId = data["topicId"] as? String ?: "",
            order = data["order"] as? Int ?: 0
        )
    }

    fun firestoreToEntity(firestoreLesson: FirestoreLesson): LessonEntity {
        return LessonEntity(
            id = firestoreLesson.id,
            lessonName = firestoreLesson.lessonName,
            topicId = firestoreLesson.topicId,
            order = firestoreLesson.order
        )
    }
}