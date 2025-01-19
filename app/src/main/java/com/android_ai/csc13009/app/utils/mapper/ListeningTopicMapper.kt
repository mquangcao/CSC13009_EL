package com.android_ai.csc13009.app.utils.mapper

import com.android_ai.csc13009.app.data.local.entity.ListeningTopicEntity
import com.android_ai.csc13009.app.data.remote.model.FirestoreListeningTopic

fun FirestoreListeningTopic.toEntity(): ListeningTopicEntity {
    return ListeningTopicEntity(
        id = this.id,
        title = this.title,
        thumbnailUrl = this.thumbnailUrl
    )
}

fun createFirestoreListeningTopicFromFirestore(id: String, title: String, thumbnailUrl: String): FirestoreListeningTopic {
    return FirestoreListeningTopic(
        id = id,
        title = title,
        thumbnailUrl = thumbnailUrl
    )
}

fun ListeningTopicEntity.toFirestore(): FirestoreListeningTopic {
    return FirestoreListeningTopic(
        id = this.id,
        title = this.title,
        thumbnailUrl = this.thumbnailUrl
    )
}