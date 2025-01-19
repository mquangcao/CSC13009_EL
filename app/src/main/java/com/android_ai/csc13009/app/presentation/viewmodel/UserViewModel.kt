package com.android_ai.csc13009.app.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android_ai.csc13009.app.data.remote.model.LoginState
import com.android_ai.csc13009.app.domain.models.UserModel
import com.android_ai.csc13009.app.data.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState> get() = _loginState

    private val _user = MutableLiveData<UserModel?>()
    val user: MutableLiveData<UserModel?> get() = _user

    fun registerUser(email: String, password: String, firstName: String, lastName: String) {
        val userModel = UserModel(
            id = "",
            fullName = "$firstName $lastName",
            joinDate = System.currentTimeMillis().toString(),
            avatar = null,
            streakCount = 0,
            level = ""
        )

        viewModelScope.launch {
            try {
                val user = userRepository.registerUser(email, password, userModel)
                if (user != null) {
                    _loginState.postValue(LoginState.Success(user))
                } else {
                    _loginState.postValue(LoginState.Error("Registration failed. Please try again."))
                }
            } catch (e: Exception) {
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
                _user.postValue(user) // Cập nhật thông tin người dùng khi đăng nhập thành công
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
                _user.postValue(user) // Cập nhật thông tin người dùng hiện tại
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

    // Thêm hàm kiểm tra và cập nhật streak
    fun checkAndUpdateStreak() {
        viewModelScope.launch {
            try {
                val updatedUser = userRepository.updateStreakIfNeeded() // Gọi hàm từ Repository
                if (updatedUser != null) {
                    _user.postValue(updatedUser) // Cập nhật thông tin người dùng
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Error updating streak: ${e.message}")
            }
        }
    }
}
