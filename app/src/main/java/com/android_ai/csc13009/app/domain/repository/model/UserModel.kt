//Model là lớp trung gian đại diện cho dữ liệu mà bạn sử dụng trong UI
package com.android_ai.csc13009.app.domain.repository.model

data class UserModel(
    val id: String,
    val fullName: String, // Kết hợp firstName + lastName
    val joinDate: String,
    val avatar: String?,
    val streakCount: Int,
    val level: String
)
