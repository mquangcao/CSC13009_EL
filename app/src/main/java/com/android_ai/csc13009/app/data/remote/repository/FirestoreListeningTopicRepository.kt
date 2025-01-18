package com.android_ai.csc13009.app.data.remote.repository

import android.util.Log
import com.android_ai.csc13009.app.data.remote.model.FirestoreListeningTopic
import com.android_ai.csc13009.app.utils.mapper.createFirestoreListeningTopicFromFirestore
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreListeningTopicRepository(private val firestore: FirebaseFirestore) {
    suspend fun getTopicList(): List<FirestoreListeningTopic> {
        return try {
            firestore.collection("listeningTopic")
                .get()
                .await()
                .documents
                .mapNotNull { document ->
                    val data = document.data ?: return@mapNotNull null
                    val title = data["title"] as? String ?: return@mapNotNull null
                    val thumbnailUrl = data["thumbnailUrl"] as? String ?: return@mapNotNull null
                    createFirestoreListeningTopicFromFirestore(document.id, title, thumbnailUrl)
                }

        } catch (e: Exception) {
            Log.e("FirestoreListeningTopicRepository", "Error fetching topics: ${e.message}")
            emptyList()
        }
    }
}