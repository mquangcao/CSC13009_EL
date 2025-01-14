package com.android_ai.csc13009.app.domain.models

import java.io.Serializable

class Question(
    var id: Int,
    var type: String,
    var answer: List<AnswerWord>,
): Serializable