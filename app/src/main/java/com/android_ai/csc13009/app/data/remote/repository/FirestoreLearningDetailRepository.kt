package com.android_ai.csc13009.app.data.remote.repository

import android.util.Log
import com.android_ai.csc13009.app.data.remote.model.FirestoreLearningDetail
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreLearningDetailRepository(private val firestore: FirebaseFirestore) {
    suspend fun createLearningDetail(userId: String, questionId: String, isCorrect : Boolean, type : String): String? {
        return try {
            val id = firestore.collection("learningDetail").document().id
            val learningDetail = FirestoreLearningDetail(
                id = id,
                date = System.currentTimeMillis().toString(),
                questionId = questionId,
                correct = isCorrect,
                userId = userId,
                type = type,
                reviewed = false)
            firestore.collection("learningDetail").document(id).set(learningDetail).await()
            id
        } catch (e: Exception) {
            Log.e("FirestoreLearningDetailRepository", "Error creating: ${e.message}")
            null
        }
    }

    suspend fun isExist(questionId: String, userId: String): Boolean {
        return try {
            val querySnapshot = firestore.collection("learningDetail")
                .whereEqualTo("questionId", questionId)
                .whereEqualTo("userId", userId)
                .get()
                .await()
            !querySnapshot.isEmpty
        } catch (e: Exception) {
            Log.e("FirestoreLearningDetailRepository", "Error checking : ${e.message}")
            false
        }
    }

    suspend fun updateLearningDetail(questionId: String, isCorrect: Boolean) : String? {
        return try {
            // Tìm document có trường questionId khớp
            val querySnapshot = firestore.collection("learningDetail")
                .whereEqualTo("questionId", questionId)
                .get()
                .await()

            if (!querySnapshot.isEmpty) {
                // Lấy document đầu tiên (hoặc duyệt nếu cần)
                val document = querySnapshot.documents[0]

                // Cập nhật document
                firestore.collection("learningDetail").document(document.id)
                    .update("isCorrect", isCorrect, "date", System.currentTimeMillis().toString())
                    .await()

                Log.d("FirestoreLearningDetailRepository", "Document updated successfully")
                document.id
            } else {
                Log.d("FirestoreLearningDetailRepository", "No matching document found")
                ""
            }

        } catch (e: Exception) {
            Log.e("FirestoreLearningDetailRepository", "Error updating: ${e.message}")
            ""
        }
    }
}