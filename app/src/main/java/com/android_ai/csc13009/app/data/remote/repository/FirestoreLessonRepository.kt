package com.android_ai.csc13009.app.data.remote.repository

import android.util.Log
import com.android_ai.csc13009.app.data.remote.model.FirestoreLesson
import com.android_ai.csc13009.app.utils.mapper.LessonMapper
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreLessonRepository(private val firestore: FirebaseFirestore) {
        suspend fun getLessonList(topicId : String) : List<FirestoreLesson> {
            return try {
                firestore.collection("lessons")
                    .whereEqualTo("topicId", topicId)
                    .get()
                    .await()
                    .documents
                    .mapNotNull { document ->
                        LessonMapper.fromFirestore(document.id, document.data ?: emptyMap())
                    }

            } catch (e: Exception) {
                Log.e("FirestoreLessonRepository", "Error fetching lessons: ${e.message}")
                emptyList()
            }
        }
}