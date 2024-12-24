package com.android_ai.csc13009.app.data.remote.model

data class FirestoreUserModel(
    val id: String = "",
    val fullName: String = "",
    val joinDate: String = "",
    val avatar: String? = null,
    val streakCount: Int = 0,
    val level: String = ""
)