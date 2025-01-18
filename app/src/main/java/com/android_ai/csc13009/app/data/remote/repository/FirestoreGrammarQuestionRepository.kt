package com.android_ai.csc13009.app.data.remote.repository

import com.android_ai.csc13009.app.data.remote.model.FirestoreGrammarQuestion
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreGrammarQuestionRepository(private val firestore: FirebaseFirestore) {

    suspend fun getQuestionsByTopicId(topicId: String): List<FirestoreGrammarQuestion> {
        val querySnapshot = firestore.collection("GrammarQuestion")
            .whereEqualTo("grammarTopicId", topicId)
            .get()
            .await()

        return querySnapshot.documents.mapNotNull { document ->
            val id = document.id
            val grammarTopicId = document.getString("grammarTopicId") ?: return@mapNotNull null
            val name = document.getString("name") ?: return@mapNotNull null
            val type = document.getString("type") ?: return@mapNotNull null

            FirestoreGrammarQuestion(id, grammarTopicId, name, type)
        }
    }

    suspend fun getQuestionById(questionId: String): FirestoreGrammarQuestion? {
        val documentSnapshot = firestore.collection("GrammarQuestion")
            .document(questionId)
            .get()
            .await()

        return documentSnapshot?.let { document ->
            val id = document.id
            val grammarTopicId = document.getString("grammarTopicId") ?: return@let null
            val name = document.getString("name") ?: return@let null
            val type = document.getString("type") ?: return@let null

            FirestoreGrammarQuestion(id, grammarTopicId, name, type)
        }
    }
}