//ViewModel giúp quản lý dữ liệu giữa Repository và UI,
// đồng thời hỗ trợ xử lý dữ liệu không bị mất khi xoay màn hình hoặc thay đổi cấu hình.
package com.android_ai.csc13009.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android_ai.csc13009.app.domain.models.UserModel
import com.android_ai.csc13009.app.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(
    private val userRepository: UserRepository,
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : ViewModel() {

    fun registerUser(email: String, password: String, firstName: String, lastName: String) {
        val userModel = UserModel(
            id = "",
            fullName = "$firstName $lastName",
            joinDate = System.currentTimeMillis().toString(),
            avatar = null,
            streakCount = 0,
            level = "Beginner"
        )

        viewModelScope.launch {
            userRepository.registerUser(email, password, userModel)
        }
    }

    fun getUserById(userId: String, callback: (UserModel?) -> Unit) {
        viewModelScope.launch {
            val user = withContext(Dispatchers.IO) { userRepository.getUserById(userId) }
            callback(user)
        }
    }
}
