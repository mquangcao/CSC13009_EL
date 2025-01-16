package com.android_ai.csc13009.app.data.remote.repository

import android.util.Log
import com.android_ai.csc13009.app.data.remote.model.FirestoreTopic
import com.android_ai.csc13009.app.utils.mapper.TagMapper.fromFirestore
import com.android_ai.csc13009.app.utils.mapper.TopicMapper
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreTopicRepository(private val firestore: FirebaseFirestore) {
    suspend fun getTopicList() : List<FirestoreTopic> {
        return try {
            firestore.collection("topic")
                .get()
                .await()
                .documents
                .mapNotNull { document ->
                    TopicMapper.fromFirestore(document.id, document.data ?: emptyMap())
                }

        } catch (e: Exception) {
            Log.e("FirestoreTopicRepository", "Error fetching topics: ${e.message}")
            emptyList()
        }
    }


}