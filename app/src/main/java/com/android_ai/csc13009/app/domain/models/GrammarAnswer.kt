package com.android_ai.csc13009.app.domain.models

data class GrammarAnswer(
    val id: String,
    val grammarQuestionId: String,
    val answer: String,
    val isCorrect: Boolean
)