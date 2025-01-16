package com.android_ai.csc13009.app.data.remote.model

data class FirestoreAnswers(
    val id: String = "",
    val questionId: String = "",
    val text: String = "",
    val isCorrect: Boolean = false,
    val imgUrl: String = ""
)