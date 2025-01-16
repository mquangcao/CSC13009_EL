package com.android_ai.csc13009.app.data.remote.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

// FirestoreGrammarLearningStatisticsRepository
class FirestoreGrammarLearningStatisticsRepository(private val firestore: FirebaseFirestore) {

    suspend fun getCorrectAnswerCount(grammarQuestionId: String, userId: String): Int {
        val snapshot = firestore.collection("GrammarLearningStatistics")
            .whereEqualTo("grammarQuestionId", grammarQuestionId)
            .whereEqualTo("userId", userId)
            .whereEqualTo("isCorrect", true)
            .get()
            .await()
        return snapshot.size()
    }

    suspend fun getTotalAnswerCount(grammarQuestionId: String, userId: String): Int {
        val snapshot = firestore.collection("GrammarLearningStatistics")
            .whereEqualTo("grammarQuestionId", grammarQuestionId)
            .whereEqualTo("userId", userId)
            .get()
            .await()
        return snapshot.size()
    }
}