package com.android_ai.csc13009.app.data.remote.repository

import android.util.Log
import com.android_ai.csc13009.app.data.remote.model.FirestoreGrammarSubtopic
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreGrammarSubtopicRepository(private val firestore: FirebaseFirestore) {

    suspend fun getSubtopicsByTopicId(topicId: String): List<FirestoreGrammarSubtopic> {
        Log.d("topicId in FirestoreGrammarSubtopicRepository", topicId)
        val querySnapshot = firestore.collection("GrammarSubtopic")
            .whereEqualTo("topicId", topicId)
            .get()
            .await()


        Log.d("querySnapshot size", querySnapshot.documents.size.toString())
        querySnapshot.documents.forEach { document ->
            Log.d("document", document.data.toString())
        }


        return querySnapshot.documents.mapNotNull { document ->
            val id = document.id
            val topicId = document.getString("topicId") ?: return@mapNotNull null
            val name = document.getString("name") ?: return@mapNotNull null
            val content = document.getString("content") ?: return@mapNotNull null
            val structures = document.getString("structures") ?: return@mapNotNull null
            val examples = document.getString("examples") ?: return@mapNotNull null

            FirestoreGrammarSubtopic(id, topicId, name, content, structures, examples)
        }
    }

    suspend fun getAllSubtopics(): List<FirestoreGrammarSubtopic> {
        val querySnapshot = firestore.collection("GrammarSubtopic")
            .get()
            .await()

        return querySnapshot.documents.mapNotNull { document ->
            val id = document.id
            val topicId = document.getString("topicId") ?: return@mapNotNull null
            val name = document.getString("name") ?: return@mapNotNull null
            val content = document.getString("content") ?: return@mapNotNull null
            val structures = document.getString("structures") ?: return@mapNotNull null
            val examples = document.getString("examples") ?: return@mapNotNull null

            FirestoreGrammarSubtopic(id, topicId, name, content, structures, examples)
        }
    }
}
