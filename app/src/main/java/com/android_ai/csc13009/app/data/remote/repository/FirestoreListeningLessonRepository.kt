package com.android_ai.csc13009.app.data.remote.repository

import android.util.Log
import com.android_ai.csc13009.app.data.remote.model.FirestoreListeningLesson
import com.android_ai.csc13009.app.utils.mapper.createFirestoreListeningLessonFromFirestore
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreListeningLessonRepository(private val firestore: FirebaseFirestore) {
    suspend fun getLessonList(topicId : String) : List<FirestoreListeningLesson> {
        return try {
            firestore.collection("listeningLesson")
                .whereEqualTo("topicId", topicId)
                .get()
                .await()
                .documents
                .mapNotNull { document ->
                    convert(document, topicId)
                }

        } catch (e: Exception) {
            Log.e("FirestoreListeningLessonRepository", "Error fetching lessons: ${e.message}")
            emptyList()
        }
    }

    private fun convert(document: DocumentSnapshot, topicId: String): FirestoreListeningLesson {
        val data = document.data
        if (data == null) {
            Log.e("FirestoreListeningLessonRepository", "Document data is null")
            return FirestoreListeningLesson("", "", "", 0)
        }
        val lessonName = data["lessonName"] as? String ?: ""
        val order: Any? = data["order"]
        var intOrder = 0
        if (order is Long) {
            intOrder = order.toInt()
        }

        return createFirestoreListeningLessonFromFirestore(document.id, lessonName, topicId, intOrder)
    }
}