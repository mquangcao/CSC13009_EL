package com.android_ai.csc13009.app.domain.models

data class Question (
    var id: Int,
    var type: String,
    var answers: List<Answer>,
    var isCorrect: Boolean?
)