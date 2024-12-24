package com.android_ai.csc13009.app.data.remote.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository(private val firebaseAuth: FirebaseAuth) {

    suspend fun registerUser(email: String, password: String): FirebaseUser? {
        val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        return authResult.user
    }

    suspend fun loginUser(email: String, password: String): FirebaseUser? {
        val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        return authResult.user
    }

    fun getCurrentUser(): FirebaseUser? = firebaseAuth.currentUser

    fun logout() = firebaseAuth.signOut()
}