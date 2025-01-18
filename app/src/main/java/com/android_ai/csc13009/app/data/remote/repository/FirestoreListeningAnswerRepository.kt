package com.android_ai.csc13009.app.data.remote.repository

import android.util.Log
import com.android_ai.csc13009.app.data.remote.model.FirestoreListeningAnswer
import com.android_ai.csc13009.app.utils.mapper.createFirestoreListeningAnswerFromFirestore
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
                    val data = document.data ?: return@mapNotNull null
                    val text = data["text"] as? String ?: return@mapNotNull null
                    val isCorrect = data["isCorrect"] as? Boolean ?: return@mapNotNull null
                    val imgUrl = data["imgUrl"] as? String ?: return@mapNotNull null
                    val id = document.id
                    createFirestoreListeningAnswerFromFirestore(id, questionId, text, isCorrect, imgUrl)
                }

        } catch (e: Exception) {
            Log.e("FirestoreLessonRepository", "Error fetching lessons: ${e.message}")
            emptyList()
        }
    }
}