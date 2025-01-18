package com.android_ai.csc13009.app.data.remote.repository

import android.util.Log
import com.android_ai.csc13009.app.data.remote.model.FirestoreListeningLesson
import com.android_ai.csc13009.app.utils.mapper.createFirestoreListeningLessonFromFirestore
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
                    val data = document.data ?: return@mapNotNull null
                    val lessonName = data["lessonName"] as? String ?: return@mapNotNull null
                    val order = data["order"] as? Int ?: return@mapNotNull null

                    createFirestoreListeningLessonFromFirestore(document.id, lessonName, topicId, order)
                }

        } catch (e: Exception) {
            Log.e("FirestoreListeningLessonRepository", "Error fetching lessons: ${e.message}")
            emptyList()
        }
    }
}