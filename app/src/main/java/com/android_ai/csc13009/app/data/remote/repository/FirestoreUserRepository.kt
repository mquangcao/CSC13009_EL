package com.android_ai.csc13009.app.data.remote.repository

import com.android_ai.csc13009.app.data.remote.model.FirestoreUserModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreUserRepository(private val firestore: FirebaseFirestore) {

    suspend fun saveUser(user: FirestoreUserModel) {
        firestore.collection("users").document(user.id).set(user).await()
    }

    suspend fun getUserById(userId: String): FirestoreUserModel? {
        val document = firestore.collection("users").document(userId).get().await()
        return document.toObject(FirestoreUserModel::class.java)
    }
}