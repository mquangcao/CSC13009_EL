package com.android_ai.csc13009.app.domain.models

import java.io.Serializable

class Question(
    var id : String,
    var question : String,
    var type : String,
    var answer : ArrayList<AnswerWord>,
): Serializable