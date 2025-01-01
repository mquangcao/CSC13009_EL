package com.android_ai.csc13009.app.data.remote.model

data class FirestoreTag(
    val userId: String = "",
    val name: String = "",
    val wordIds: List<Int> = emptyList()
)