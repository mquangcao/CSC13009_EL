package com.android_ai.csc13009.app.domain.models

data class Tag(
    val id: String,
    val userId: String,
    val name: String,
    val wordIds: List<Int>? = null
)
