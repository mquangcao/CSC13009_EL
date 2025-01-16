package com.android_ai.csc13009.app.utils.mapper

import com.android_ai.csc13009.app.data.local.entity.AnswersEntity
import com.android_ai.csc13009.app.data.remote.model.FirestoreAnswers
import com.android_ai.csc13009.app.domain.models.Answer
import com.android_ai.csc13009.app.domain.models.Word
import com.android_ai.csc13009.app.domain.models.WordModel


fun AnswersEntity.toDomain(answerWord: WordModel) : Answer {
    return Answer(
        id = 1,
        answerWord = TODO(),
        isCorrect = this.isCorrect,
        answer = TODO(),
        thumbNails = TODO()
    )
}

object AnswerMapper {
    fun fromFirestore(id: String, data: Map<String, Any>): FirestoreAnswers {
        return FirestoreAnswers(
            id = id,
            questionId = data["questionId"] as? String ?: "",
            text = data["text"] as? String ?: "",
            isCorrect = data["isCorrect"] as? Boolean ?: false,
            imgUrl = data["imgUrl"] as? String ?: ""
        )
    }

    fun firestoreToEntity(firestoreAnswers: FirestoreAnswers): AnswersEntity {
        return AnswersEntity(
            id = firestoreAnswers.id,
            questionId = firestoreAnswers.questionId,
            text = firestoreAnswers.text,
            isCorrect = firestoreAnswers.isCorrect,
            imgUrl = firestoreAnswers.imgUrl
        )
    }
}