package com.android_ai.csc13009.app.utils.mapper;

import com.android_ai.csc13009.app.data.local.entity.ListeningQuestionEntity
import com.android_ai.csc13009.app.data.remote.model.FirestoreListeningQuestion

fun ListeningQuestionEntity.toFirestore() : FirestoreListeningQuestion {
    return FirestoreListeningQuestion(
        id = this.id,
        question = this.question,
        lessonId = this.lessonId,
        level = this.level,
        type = this.type,
        audioTranscript = "",
    )
}

fun createFirestoreListeningQuestionFromFirestore(
    id: String, question: String, lessonId: String, level: String, type: String, audioTranscript: String)
    : FirestoreListeningQuestion {
    return FirestoreListeningQuestion(
        id = id,
        question = question,
        lessonId = lessonId,
        level = level,
        audioTranscript = audioTranscript,
        type = type
    )
}

fun FirestoreListeningQuestion.toEntity() : ListeningQuestionEntity {
    return ListeningQuestionEntity(
        id = this.id,
        question = this.question,
        lessonId = this.lessonId,
        level = this.level,
        type = this.type,
        audioTranscript = this.audioTranscript,
    )
}
