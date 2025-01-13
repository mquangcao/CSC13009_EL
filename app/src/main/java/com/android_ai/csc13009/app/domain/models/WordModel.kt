package com.android_ai.csc13009.app.domain.models

data class WordModel(
    val id: Int,
    val word: String,
    val pronunciation: String?,
    val details: String
)