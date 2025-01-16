package com.android_ai.csc13009.app.data.remote.repository

import com.android_ai.csc13009.app.data.remote.model.FirestoreGrammarTopic
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreGrammarTopicRepository(private val firestore: FirebaseFirestore) {

    suspend fun getTopicsByLevel(levelId: String): List<FirestoreGrammarTopic> {
        val querySnapshot = firestore.collection("GrammarTopic") // Update collection name
            .whereEqualTo("levelId", levelId)
            .get()
            .await()

        return querySnapshot.documents.mapNotNull { document ->
            val levelId = document.getString("levelId") ?: return@mapNotNull null
            val name = document.getString("name") ?: return@mapNotNull null
            val id = document.id  // Get the Firestore document ID
            FirestoreGrammarTopic(id, levelId, name)
        }
    }

    suspend fun getTopicByName(topicName: String): FirestoreGrammarTopic? {
        val querySnapshot = firestore.collection("GrammarTopic") // Update collection name
            .whereEqualTo("name", topicName)
            .get()
            .await()

        val document = querySnapshot.documents.firstOrNull() ?: return null
        val levelId = document.getString("levelId") ?: return null
        val name = document.getString("name") ?: return null
        val id = document.id  // Get the Firestore document ID

        return FirestoreGrammarTopic(id, levelId, name)
    }

    suspend fun getAllTopics(): List<FirestoreGrammarTopic> {
        val querySnapshot = firestore.collection("GrammarTopic") // Update collection name
            .get()
            .await()

        return querySnapshot.documents.mapNotNull { document ->
            val levelId = document.getString("levelId") ?: return@mapNotNull null
            val name = document.getString("name") ?: return@mapNotNull null
            val id = document.id  // Get the Firestore document ID

            FirestoreGrammarTopic(id, levelId, name)
        }
    }
}
