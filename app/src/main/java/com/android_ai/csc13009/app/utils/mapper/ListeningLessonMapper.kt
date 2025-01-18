package com.android_ai.csc13009.app.utils.mapper

import com.android_ai.csc13009.app.data.local.entity.ListeningLessonEntity
import com.android_ai.csc13009.app.data.remote.model.FirestoreListeningLesson

fun ListeningLessonEntity.toFirestore() : FirestoreListeningLesson {
    return FirestoreListeningLesson(
        id = this.id,
        lessonName = this.lessonName,
        topicId = this.topicId,
        order = this.order
    )
}

fun createFirestoreListeningLessonFromFirestore(
    id: String, lessonName: String, topicId: String, order: Int
)
    : FirestoreListeningLesson {
    return FirestoreListeningLesson(
        id = id,
        lessonName = lessonName,
        topicId = topicId,
        order = order,
    )
}

fun FirestoreListeningLesson.toEntity() : ListeningLessonEntity {
    return ListeningLessonEntity(
        id = this.id,
        lessonName = this.lessonName,
        topicId = this.topicId,
        order = this.order
    )
}