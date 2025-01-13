package com.android_ai.csc13009.app.data.remote.model

import com.android_ai.csc13009.app.domain.models.UserModel

sealed class LoginState {
    data class Success(val user: UserModel) : LoginState()
    data class Error(val message: String) : LoginState()
    object Loading : LoginState()
}
