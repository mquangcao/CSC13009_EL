package com.android_ai.csc13009.app.domain.models

data class GrammarAnswer(
    val grammarAnswerId: Int,
    val grammarQuestionId: Int,
    val answer: String,
    val isCorrect: Boolean

)