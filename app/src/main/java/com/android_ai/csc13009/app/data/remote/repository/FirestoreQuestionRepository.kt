package com.android_ai.csc13009.app.data.remote.repository

import android.util.Log
import com.android_ai.csc13009.app.data.remote.model.FirestoreLesson
import com.android_ai.csc13009.app.data.remote.model.FirestoreQuestion
import com.android_ai.csc13009.app.utils.mapper.LessonMapper
import com.android_ai.csc13009.app.utils.mapper.QuestionMapper
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreQuestionRepository(private val firestore: FirebaseFirestore) {
    suspend fun getQuestionList(lessonId : String, level : String) : List<FirestoreQuestion> {
        return try {
            firestore.collection("wordQuestion")
                .whereEqualTo("lessonId", lessonId)
                .whereEqualTo("level", level)
                .get()
                .await()
                .documents
                .mapNotNull { document ->
                    QuestionMapper.fromFirestore(document.id, document.data ?: emptyMap())
                }

        } catch (e: Exception) {
            Log.e("FirestoreLessonRepository", "Error fetching lessons: ${e.message}")
            emptyList()
        }
    }

    suspend fun getQuestionById(questionId: String): FirestoreQuestion? {
        return try {
            val documentSnapshot = firestore.collection("wordQuestion")
                .document(questionId)
                .get()
                .await()

            // Use a mapper to convert the document into your desired object
            if (documentSnapshot.exists()) {
                QuestionMapper.fromFirestore(
                    documentSnapshot.id,
                    documentSnapshot.data ?: emptyMap()
                )
            } else {
                null // Return null if the document does not exist
            }
        } catch (e: Exception) {
            Log.e("FirestoreLessonRepository", "Error fetching question: ${e.message}")
            null // Return null in case of an error
        }
    }
}