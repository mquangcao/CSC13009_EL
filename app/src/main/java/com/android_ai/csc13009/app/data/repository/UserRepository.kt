//Repository là lớp trung gian để truy vấn từ UserDao và chuyển đổi dữ liệu từ Entity thành Model
package com.android_ai.csc13009.app.data.repository

import android.util.Log
import com.android_ai.csc13009.app.data.remote.repository.FirebaseAuthRepository
import com.android_ai.csc13009.app.data.remote.repository.FirestoreUserRepository
import com.android_ai.csc13009.app.domain.models.UserModel
import com.android_ai.csc13009.app.domain.repository.IUserRepository
import com.android_ai.csc13009.app.utils.getCurrentDate
import com.android_ai.csc13009.app.utils.isSameDay
import com.android_ai.csc13009.app.utils.mapper.UserMapper


class UserRepository(
    private val authRepository: FirebaseAuthRepository,
    private val firestoreRepository: FirestoreUserRepository
) : IUserRepository {

    override suspend fun registerUser(email: String, password: String, userModel: UserModel) : UserModel? {
        return try {
            val firebaseUser = authRepository.registerUser(email, password)
            firebaseUser?.let {
                val newUser = userModel.copy(id = it.uid)
                firestoreRepository.saveUser(UserMapper.toFirestore(newUser))
                newUser
            }
        } catch (e: Exception) {
            // Log the error and handle it gracefully
            Log.e("UserRepository", "Login failed: ${e.message}")
            null // Return null to indicate failure
        }
    }

    // Login a user
    override suspend fun loginUser(email: String, password: String): UserModel? {
        return try {
            // Authenticate the user with FirebaseAuth
            val firebaseUser = authRepository.loginUser(email, password)

            firebaseUser?.let { user ->
                // Fetch the corresponding Firestore user data
                val firestoreUser = firestoreRepository.getUserById(user.uid)

                // Map Firestore user data to domain model and return it
                firestoreUser?.let { UserMapper.toDomain(it) }
            }
        } catch (e: Exception) {
            // Log the error and handle it gracefully
            Log.e("UserRepository", "Login failed: ${e.message}")
            null // Return null to indicate failure
        }
    }

    // Get the current logged-in user
    override suspend fun getCurrentUser(): UserModel? {
        val firebaseUser = authRepository.getCurrentUser()
        return firebaseUser?.let {
            val firestoreUser = firestoreRepository.getUserById(it.uid)
            firestoreUser?.let { UserMapper.toDomain(firestoreUser) }
        }
    }

    // Logout the user
    override fun logout() {
        authRepository.logout()
    }

    suspend fun updateStreakIfNeeded(): UserModel? {
        val currentUser = authRepository.getCurrentUser() ?: return null
        val firestoreUser = firestoreRepository.getUserById(currentUser.uid) ?: return null

        // Lấy ngày hiện tại
        val today = getCurrentDate()
        val lastCheckDate = firestoreUser.joinDate // Sử dụng joinDate hoặc trường thời gian sẵn có

        // Nếu ngày hiện tại khác với ngày kiểm tra trước đó
        if (!isSameDay(today, lastCheckDate)) {
            // Tăng streak
            val newStreakCount = firestoreUser.streakCount + 1

            // Cập nhật streak vào Firestore
            firestoreRepository.updateStreak(
                userId = firestoreUser.id,
                streakCount = newStreakCount,
                lastLoginDate = today // Chỉ cập nhật Firestore
            )

            // Tạo bản cập nhật và trả về
            val updatedUser = firestoreUser.copy(streakCount = newStreakCount)
            return UserMapper.toDomain(updatedUser)
        }

        // Nếu ngày giống nhau, trả về người dùng hiện tại
        return UserMapper.toDomain(firestoreUser)
    }

}