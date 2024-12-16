//Repository là lớp trung gian để truy vấn từ UserDao và chuyển đổi dữ liệu từ Entity thành Model
package com.android_ai.csc13009

import com.android_ai.csc13009.app.data.local.dao.UserDao
import com.android_ai.csc13009.app.data.local.entity.UserEntity
import com.android_ai.csc13009.app.domain.repository.model.UserModel


class UserRepository(private val userDao: UserDao) {

    // Lấy thông tin người dùng từ database và chuyển thành UserModel
    suspend fun getUserById(userId: String): UserModel? {
        val userEntity: UserEntity? = userDao.getUserById(userId)
        return userEntity?.let { entity ->
            UserModel(
                id = entity.id,
                fullName = "${entity.firstName} ${entity.lastName}",
                joinDate = entity.joinDate,
                avatar = entity.avatar,
                streakCount = entity.streakCount,
                level = entity.level
            )
        }
    }
}
