package com.android_ai.csc13009.app.utils.mapper

import com.android_ai.csc13009.app.data.local.entity.ChapterEntity
import com.android_ai.csc13009.app.data.remote.model.FirestoreTopic
import com.android_ai.csc13009.app.domain.models.Tag

object TopicMapper {
    fun fromFirestore(documentId: String, data: Map<String, Any>) : FirestoreTopic? {
        return try {
            FirestoreTopic(
                id = documentId,
                title = data["title"] as? String ?: return null,
                thumbnailUrl = data["thumbnailUrl"] as? String ?: return null
            )
        } catch (e: Exception) {
            return null
        }
    }

    fun firestoreToEntity(firestoreTopic: FirestoreTopic): ChapterEntity {
        return ChapterEntity(
            id = firestoreTopic.id,
            title = firestoreTopic.title,
            thumbnailUrl = firestoreTopic.thumbnailUrl
        )
    }


}