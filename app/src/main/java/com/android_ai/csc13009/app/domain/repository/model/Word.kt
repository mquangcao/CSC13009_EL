package com.android_ai.csc13009.app.domain.repository.model

data class Word(
    val id: Int,
    val word: String,
    val pronunciation: String?,
    val details: String
)