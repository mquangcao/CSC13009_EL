package com.android_ai.csc13009.app.domain.repository

import com.android_ai.csc13009.app.domain.models.UserModel

interface IUserRepository {
    suspend fun registerUser(email: String, password: String, userModel: UserModel)
    suspend fun getUserById(userId: String): UserModel?
}