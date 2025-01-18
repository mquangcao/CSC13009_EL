package com.android_ai.csc13009.app.data.remote.repository

import android.util.Log
import com.android_ai.csc13009.app.data.remote.model.FirestoreListeningQuestion
import com.android_ai.csc13009.app.utils.mapper.createFirestoreListeningQuestionFromFirestore
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreListeningQuestionRepository(private val firestore: FirebaseFirestore) {
    suspend fun getQuestionList(lessonId : String, level : String) : List<FirestoreListeningQuestion> {
        return try {
            firestore.collection("listeningQuestion")
                .whereEqualTo("lessonId", lessonId)
                .whereEqualTo("level", level)
                .get()
                .await()
                .documents
                .mapNotNull { document ->
                    convert(document, lessonId, level)
                }

        } catch (e: Exception) {
            Log.e("FirestoreListeningLessonRepository", "Error fetching lessons: ${e.message}")
            emptyList()
        }
    }

    private fun convert(document: DocumentSnapshot, lessonId: String, level: String): FirestoreListeningQuestion {
        val data = document.data
        val question = data?.get("question") as? String ?: ""
        val type = data?.get("type") as? String ?: ""
        val id = document.id
        return createFirestoreListeningQuestionFromFirestore(id, question, lessonId, level, type)
    }
}