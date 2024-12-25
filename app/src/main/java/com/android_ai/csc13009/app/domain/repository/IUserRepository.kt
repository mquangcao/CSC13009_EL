package com.android_ai.csc13009.app.domain.repository

import com.android_ai.csc13009.app.domain.models.UserModel

interface IUserRepository {
    suspend fun registerUser(email: String, password: String, userModel: UserModel) : UserModel?
    suspend fun loginUser(email: String, password: String) : UserModel?
    suspend fun getCurrentUser(): UserModel?
    fun logout()
}