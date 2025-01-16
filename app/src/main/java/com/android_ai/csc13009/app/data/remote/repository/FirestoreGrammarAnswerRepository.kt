package com.android_ai.csc13009.app.data.remote.repository

import com.android_ai.csc13009.app.data.remote.model.FirestoreGrammarAnswer
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreGrammarAnswerRepository(private val firestore: FirebaseFirestore) {

    suspend fun getAnswersByQuestionId(questionId: String): List<FirestoreGrammarAnswer> {
        val querySnapshot = firestore.collection("GrammarAnswer")
            .whereEqualTo("grammarQuestionId", questionId)
            .get()
            .await()

        return querySnapshot.documents.map { document ->
            val data = document.data ?: return@map FirestoreGrammarAnswer() // Nếu không có dữ liệu thì trả về đối tượng mặc định
            FirestoreGrammarAnswer(
                id = data["id"] as? String ?: "",
                grammarQuestionId = data["grammarQuestionId"] as? String ?: "",
                answer = data["answer"] as? String ?: "",
                isCorrect = data["isCorrect"] as? Boolean ?: false
            )
        }
    }

    suspend fun getCorrectAnswerByQuestionId(questionId: String): FirestoreGrammarAnswer? {
        val querySnapshot = firestore.collection("GrammarAnswer")
            .whereEqualTo("grammarQuestionId", questionId)
            .whereEqualTo("isCorrect", true)
            .get()
            .await()

        return querySnapshot.documents.firstOrNull()?.toObject(FirestoreGrammarAnswer::class.java)
    }
}

