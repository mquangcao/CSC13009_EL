package com.android_ai.csc13009.app.data.remote.repository

import com.android_ai.csc13009.app.data.remote.model.FirestoreGrammarLevel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreGrammarLevelRepository(private val firestore: FirebaseFirestore) {
    suspend fun getAllLevels(): List<FirestoreGrammarLevel> {
        return firestore.collection("GrammarLevel")
            .get()
            .await()
            .toObjects(FirestoreGrammarLevel::class.java)
    }

    suspend fun getLevelByName(levelName: String): FirestoreGrammarLevel? {
        val querySnapshot = firestore.collection("GrammarLevel")
            .whereEqualTo("name", levelName)
            .get()
            .await()

        return querySnapshot.documents.firstOrNull()?.toObject(FirestoreGrammarLevel::class.java)
    }
}