package com.android_ai.csc13009.app.data.remote.repository

import android.util.Log
import com.android_ai.csc13009.app.data.remote.model.FirestoreLessonProgress
import com.android_ai.csc13009.app.data.remote.model.FirestoreTag
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreProgressRepository(private val firestore: FirebaseFirestore) {
    suspend fun createLessonFinished(userId: String, lessonId: String, totalQuestion : Int, questionSuccess : Int): String? {
        return try {
            val id = firestore.collection("lessonProgress").document().id
            val lessonProgress = FirestoreLessonProgress(id, lessonId, userId, totalQuestion, questionSuccess)
            firestore.collection("lessonProgress").document(id).set(lessonProgress).await()
            id
        } catch (e: Exception) {
            Log.e("FirestoreTagRepository", "Error creating tag: ${e.message}")
            null
        }
    }

    suspend fun getAllLessonProgress(userId: String): List<FirestoreLessonProgress> {
        return try {
            Log.d("FirestoreProgressRepository", "getAllLessonProgress: quangcao")
            firestore.collection("lessonProgress")
                .whereEqualTo("userId", userId)
                .get()
                .await()
                .documents.mapNotNull {
                    FirestoreLessonProgress(
                        it.id,
                        it["lessonId"] as? String ?: "",
                        it["userId"] as? String ?: "",
                        it["totalQuestion"] as? Int ?: 0,
                        it["questionSuccess"] as? Int ?: 0
                    )
                }
        } catch (e: Exception) {
            Log.e("FirestoreTagRepository", "Error getting all lesson progress: ${e.message}")
            emptyList()
        }
    }

    suspend fun isExist(lessonId: String, userId: String): Boolean {
        return try {
            val querySnapshot = firestore.collection("lessonProgress")
                .whereEqualTo("lessonId", lessonId)
                .whereEqualTo("userId", userId)
                .get()
                .await()
            !querySnapshot.isEmpty
        } catch (e: Exception) {
            Log.e("FirestoreTagRepository", "Error checking lesson progress: ${e.message}")
            false
        }
    }

    suspend fun updateLessonFinished(lessonProgressId: String, totalQuestion : Int, questionSuccess : Int) {
        try {
            firestore.collection("lessonProgress").document(lessonProgressId)
                .update("totalQuestion", totalQuestion, "questionSuccess", questionSuccess)
                .await()
        } catch (e: Exception) {
            Log.e("FirestoreTagRepository", "Error updating lesson progress: ${e.message}")
        }
    }
}