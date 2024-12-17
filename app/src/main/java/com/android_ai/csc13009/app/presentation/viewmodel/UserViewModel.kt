//ViewModel giúp quản lý dữ liệu giữa Repository và UI,
// đồng thời hỗ trợ xử lý dữ liệu không bị mất khi xoay màn hình hoặc thay đổi cấu hình.
package com.android_ai.csc13009.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android_ai.csc13009.app.domain.repository.model.UserModel
import com.android_ai.csc13009.app.domain.repository.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun getUserById(userId: String, callback: (UserModel?) -> Unit) {
        viewModelScope.launch {
            val user = withContext(Dispatchers.IO) { userRepository.getUserById(userId) }
            callback(user)
        }
    }
}
