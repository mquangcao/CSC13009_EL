package com.android_ai.csc13009.app.utils.mapper

import com.android_ai.csc13009.app.data.local.entity.LearningDetailEntity
import com.android_ai.csc13009.app.data.local.entity.QuestionsEntity
import com.android_ai.csc13009.app.data.remote.model.FirestoreQuestion
import com.android_ai.csc13009.app.domain.models.Answer
import com.android_ai.csc13009.app.domain.models.Question

fun QuestionsEntity.toDomain(answers: List<Answer>, isCorrect: Boolean): Question {
    return Question(
        id = TODO(),
        type = this.type,
        question = TODO(),
        answer = TODO()
    )
}

object QuestionMapper {
    fun fromFirestore(id: String, data: Map<String, Any>): FirestoreQuestion {
        return FirestoreQuestion(
            id = id,
            type = data["type"] as? String ?: "",
            question = data["question"] as? String ?: "",
            lessonId = data["lessonId"] as? String ?: "",
            level = data["level"] as? String ?: ""
        )
    }

    fun firestoreToEntity(firestoreQuestion: FirestoreQuestion): QuestionsEntity {
        return QuestionsEntity(
            id = firestoreQuestion.id,
            type = firestoreQuestion.type,
            question = firestoreQuestion.question,
            lessonId = firestoreQuestion.lessonId,
            level = firestoreQuestion.level
        )
    }
}