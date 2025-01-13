package com.android_ai.csc13009.app.domain.models

data class UserModel(
    val id: String,
    val fullName: String, // Kết hợp firstName + lastName
    val joinDate: String,
    val avatar: String?,
    val streakCount: Int,
    val level: String
)
