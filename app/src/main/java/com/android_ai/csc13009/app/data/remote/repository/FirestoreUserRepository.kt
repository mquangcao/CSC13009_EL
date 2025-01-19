package com.android_ai.csc13009.app.data.remote.repository

import android.util.Log
import com.android_ai.csc13009.app.data.remote.model.FirestoreUserModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreUserRepository(private val firestore: FirebaseFirestore) {

    suspend fun saveUser(user: FirestoreUserModel): FirestoreUserModel? {
        return try {
            // Set the user in Firestore using the document ID from the user model
            firestore.collection("users").document(user.id).set(user).await()

            // Fetch the document to verify it was saved correctly
            val documentSnapshot = firestore.collection("users").document(user.id).get().await()
            if (documentSnapshot.exists()) {
                documentSnapshot.toObject(FirestoreUserModel::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("FirestoreUserRepository", "Error saving user: ${e.message}")
            null
        }
    }

    suspend fun getUserById(userId: String): FirestoreUserModel? {
        return try {
            val document = firestore.collection("users").document(userId).get().await()
            if (document.exists()) {
                document.toObject(FirestoreUserModel::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("FirestoreUserRepository", "Error fetching user: ${e.message}")
            null
        }
    }

    suspend fun updateStreak(userId: String, streakCount: Int, lastLoginDate: String): Boolean {
        return try {
            val updates = mapOf(
                "streakCount" to streakCount
            )
            firestore.collection("users").document(userId).update(updates).await()
            true
        } catch (e: Exception) {
            Log.e("FirestoreUserRepository", "Error updating streak: ${e.message}")
            false
        }
    }

}