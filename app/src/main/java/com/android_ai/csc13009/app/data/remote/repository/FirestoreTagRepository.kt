package com.android_ai.csc13009.app.data.remote.repository

import android.util.Log
import com.android_ai.csc13009.app.data.remote.model.FirestoreTag
import com.android_ai.csc13009.app.domain.models.Tag
import com.android_ai.csc13009.app.utils.mapper.TagMapper
import com.android_ai.csc13009.app.utils.mapper.TagMapper.fromFirestore
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreTagRepository(private val firestore: FirebaseFirestore) {

    suspend fun createTag(userId: String, tagName: String): String? {
        return try {
            val tagId = firestore.collection("tags").document().id
            val tag = FirestoreTag(userId = userId, name = tagName)
            firestore.collection("tags").document(tagId).set(tag).await()
            tagId
        } catch (e: Exception) {
            Log.e("FirestoreTagRepository", "Error creating tag: ${e.message}")
            null
        }
    }

    suspend fun addWordToTag(tagId: String, wordId: Int) {
        try {
            firestore.collection("tags").document(tagId)
                .update("wordIds", FieldValue.arrayUnion(wordId))
                .await()
        } catch (e: Exception) {
            Log.e("FirestoreTagRepository", "Error adding word to tag: ${e.message}")
        }
    }

    suspend fun getUserTags(userId: String): List<Tag> {
        return try {
            firestore.collection("tags")
                .whereEqualTo("userId", userId)
                .get()
                .await()
                .documents
                .mapNotNull { document ->
                    fromFirestore(document.id, document.data ?: emptyMap())
                }
        } catch (e: Exception) {
            Log.e("FirestoreTagRepository", "Error fetching user tags: ${e.message}")
            emptyList()
        }
    }

    suspend fun deleteTag(tagId: String) {
        try {
            firestore.collection("tags").document(tagId).delete().await()
        } catch (e: Exception) {
            Log.e("FirestoreTagRepository", "Error deleting tag: ${e.message}")
        }
    }

    suspend fun getTagById(tagId: String): Tag? {
        return try {
            val documentSnapshot = firestore.collection("tags").document(tagId).get().await()
            val data = documentSnapshot.data
            if (data != null) {
                TagMapper.fromFirestore(documentSnapshot.id, data)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("FirestoreTagRepository", "Error fetching tag by ID: ${e.message}")
            null
        }
    }

    suspend fun deleteWordFromTag(wordId: Int, tagId: String) {
        try {
            val tagRef = firestore.collection("tags").document(tagId)
            firestore.runTransaction { transaction ->
                val tagSnapshot = transaction.get(tagRef)

                // Retrieve the current word IDs as a list of numbers
                val currentWordIds = (tagSnapshot.get("wordIds") as? List<*>)?.mapNotNull {
                    (it as? Number)?.toInt()
                } ?: emptyList()

                // Check if the wordId exists and update if necessary
                if (wordId in currentWordIds) {
                    val updatedWordIds = currentWordIds.toMutableList().apply { remove(wordId) }
                    transaction.update(tagRef, "wordIds", updatedWordIds)
                }
            }
            Log.d("FirestoreTagRepository", "Word with ID $wordId successfully removed from tag $tagId.")
        } catch (e: Exception) {
            Log.e("FirestoreTagRepository", "Error deleting word from tag: ${e.message}")
        }
    }

    suspend fun updateTagName(tagId: String, newName: String) {
        try {
            val tagRef = firestore.collection("tags").document(tagId)
            tagRef.update("name", newName).await()
        } catch (e: Exception) {
            throw Exception("Failed to update tag name: ${e.message}")
        }
    }
}
