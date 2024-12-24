//Repository là lớp trung gian để truy vấn từ UserDao và chuyển đổi dữ liệu từ Entity thành Model
package com.android_ai.csc13009.app.data.repository

import com.android_ai.csc13009.app.data.remote.repository.FirebaseAuthRepository
import com.android_ai.csc13009.app.data.remote.repository.FirestoreUserRepository
import com.android_ai.csc13009.app.domain.models.UserModel
import com.android_ai.csc13009.app.domain.repository.IUserRepository
import com.android_ai.csc13009.app.utils.mapper.UserMapper


class UserRepository(
    private val authRepository: FirebaseAuthRepository,
    private val firestoreRepository: FirestoreUserRepository
) : IUserRepository {

    override suspend fun registerUser(email: String, password: String, userModel: UserModel) {
        val firebaseUser = authRepository.registerUser(email, password)
        firebaseUser?.let {
            val firestoreUserModel = UserMapper.toFirestore(userModel.copy(id = it.uid))
            firestoreRepository.saveUser(firestoreUserModel)
        }
    }

    override suspend fun getUserById(userId: String): UserModel? {
        val firestoreUserModel = firestoreRepository.getUserById(userId)
        return firestoreUserModel?.let { UserMapper.toDomain(it) }
    }
}