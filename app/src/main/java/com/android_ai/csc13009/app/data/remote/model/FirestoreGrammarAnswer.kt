package com.android_ai.csc13009.app.data.remote.model

data class FirestoreGrammarAnswer (
    val id: String = "",
    val grammarQuestionId: String = "",
    val answer: String = "",
    val isCorrect: Boolean = false
)