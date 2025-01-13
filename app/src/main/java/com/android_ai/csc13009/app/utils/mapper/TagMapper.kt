package com.android_ai.csc13009.app.utils.mapper

import android.util.Log
import com.android_ai.csc13009.app.domain.models.Tag

object TagMapper {

    // Converts a domain Tag to a Firestore map
    fun toFirestore(tag: Tag): Map<String, Any?> {
        return mapOf(
            "userId" to tag.userId,
            "name" to tag.name,
            "wordIds" to (tag.wordIds ?: emptyList<Int>()) // Handle nullability by providing an empty list
        )
    }

    // Converts a Firestore document snapshot to a domain Tag
    fun fromFirestore(documentId: String, data: Map<String, Any>): Tag? {
        return try {
            // Log raw data for debugging
            Log.d("TagMapper", "Mapping document: $documentId with data: $data")

            // Get wordIds as a list of any type
            val wordIdsData = data["wordIds"] as? List<*>

            // Map the wordIds to Int, using `toInt()` to safely convert Numbers
            val wordIds = wordIdsData?.mapNotNull {
                (it as? Number)?.toInt() // Convert Number to Int
            } ?: emptyList()

            Tag(
                id = documentId,
                userId = data["userId"] as? String ?: return null,
                name = data["name"] as? String ?: return null,
                wordIds = wordIds
            )
        } catch (e: Exception) {
            Log.e("TagMapper", "Error mapping Firestore document to Tag: ${e.message}")
            null
        }
    }
}
