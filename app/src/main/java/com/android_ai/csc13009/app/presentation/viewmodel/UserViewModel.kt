//ViewModel giúp quản lý dữ liệu giữa Repository và UI,
// đồng thời hỗ trợ xử lý dữ liệu không bị mất khi xoay màn hình hoặc thay đổi cấu hình.
package com.android_ai.csc13009.app.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android_ai.csc13009.app.data.remote.model.LoginState
import com.android_ai.csc13009.app.domain.models.UserModel
import com.android_ai.csc13009.app.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState> get() = _loginState

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
            try {
                val user = userRepository.registerUser(email, password, userModel)
                if (user != null) {
                    // Post success state with the registered user
                    _loginState.postValue(LoginState.Success(user))
                } else {
                    // Post error state if registration failed
                    _loginState.postValue(LoginState.Error("Registration failed. Please try again."))
                }
            } catch (e: Exception) {
                // Handle unexpected exceptions gracefully
                Log.e("RegisterViewModel", "Error during registration: ${e.message}")
                _loginState.postValue(LoginState.Error("An unexpected error occurred: ${e.message}"))
            }
        }
    }

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            val user = userRepository.loginUser(email, password)
            if (user != null) {
                _loginState.postValue(LoginState.Success(user))
            } else {
                _loginState.postValue(LoginState.Error("Login failed. Please check your credentials."))
            }
        }
    }

    fun getCurrentUser() {
        viewModelScope.launch {
            val user = userRepository.getCurrentUser()
            if (user != null) {
                _loginState.postValue(LoginState.Success(user))
            } else {
                _loginState.postValue(LoginState.Error("Get failed. Please check your brain."))
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }
}
