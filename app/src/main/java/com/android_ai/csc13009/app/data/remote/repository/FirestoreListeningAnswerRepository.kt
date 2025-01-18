package com.android_ai.csc13009.app.data.remote.repository

import android.util.Log
import com.android_ai.csc13009.app.data.remote.model.FirestoreListeningAnswer
import com.android_ai.csc13009.app.utils.mapper.createFirestoreListeningAnswerFromFirestore
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreListeningAnswerRepository(private val firestore: FirebaseFirestore) {
    suspend fun getAnswerList(questionId : String) : List<FirestoreListeningAnswer> {
        return try {
            firestore.collection("listeningAnswer")
                .whereEqualTo("questionId", questionId)
                .get()
                .await()
                .documents
                .mapNotNull { document ->
                    convert(document, questionId)
                }

        } catch (e: Exception) {
            Log.e("FirestoreLessonRepository", "Error fetching lessons: ${e.message}")
            emptyList()
        }
    }

    private fun convert(document: DocumentSnapshot, questionId: String): FirestoreListeningAnswer {
        val data = document.data
        val text = data?.get("text") as? String ?: ""
        val isCorrect = data?.get("isCorrect") as? Boolean ?: false
        val imgUrl = data?.get("imgUrl") as? String ?: ""
        val id = document.id

        return createFirestoreListeningAnswerFromFirestore(id, questionId, text, isCorrect, imgUrl)
    }
}