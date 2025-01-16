package com.android_ai.csc13009.app.data.remote.repository

import android.util.Log
import com.android_ai.csc13009.app.data.remote.model.FirestoreAnswers
import com.android_ai.csc13009.app.data.remote.model.FirestoreQuestion
import com.android_ai.csc13009.app.utils.mapper.AnswerMapper
import com.android_ai.csc13009.app.utils.mapper.QuestionMapper
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreAnswersRepository(private val firestore: FirebaseFirestore) {
    suspend fun getAnswerList(questionId : String) : List<FirestoreAnswers> {
        return try {
            firestore.collection("answers")
                .whereEqualTo("questionId", questionId)
                .get()
                .await()
                .documents
                .mapNotNull { document ->
                    AnswerMapper.fromFirestore(document.id, document.data ?: emptyMap())
                }

        } catch (e: Exception) {
            Log.e("FirestoreLessonRepository", "Error fetching lessons: ${e.message}")
            emptyList()
        }
    }
}