package com.android_ai.csc13009.app.utils.mapper

import com.android_ai.csc13009.app.data.remote.model.FirestoreUserModel
import com.android_ai.csc13009.app.domain.models.UserModel

object UserMapper {
    fun toDomain(firestoreModel: FirestoreUserModel): UserModel = UserModel(
        id = firestoreModel.id,
        fullName = firestoreModel.fullName,
        joinDate = firestoreModel.joinDate,
        avatar = firestoreModel.avatar,
        streakCount = firestoreModel.streakCount,
        level = firestoreModel.level
    )

    fun toFirestore(domainModel: UserModel): FirestoreUserModel = FirestoreUserModel(
        id = domainModel.id,
        fullName = domainModel.fullName,
        joinDate = domainModel.joinDate,
        avatar = domainModel.avatar,
        streakCount = domainModel.streakCount,
        level = domainModel.level
    )
}