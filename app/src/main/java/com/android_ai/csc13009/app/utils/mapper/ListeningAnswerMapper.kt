package com.android_ai.csc13009.app.utils.mapper

import com.android_ai.csc13009.app.data.local.entity.ListeningAnswerEntity
import com.android_ai.csc13009.app.data.remote.model.FirestoreListeningAnswer

fun ListeningAnswerEntity.toFirestore() : FirestoreListeningAnswer {
    return FirestoreListeningAnswer(
        id = this.id,
        questionId = this.questionId,
        text = this.text,
        isCorrect = this.isCorrect,
        imgUrl = this.imgUrl
    )
}

fun createFirestoreListeningAnswerFromFirestore(
    id: String, questionId: String, text: String, isCorrect: Boolean, imgUrl: String)
    : FirestoreListeningAnswer {
    return FirestoreListeningAnswer(
        id = id,
        questionId = questionId,
        text = text,
        isCorrect = isCorrect,
        imgUrl = imgUrl
    )
}

fun FirestoreListeningAnswer.toEntity() : ListeningAnswerEntity {
    return ListeningAnswerEntity(
        id = this.id,
        questionId = this.questionId,
        text = this.text,
        isCorrect = this.isCorrect,
        imgUrl = this.imgUrl
    )
}