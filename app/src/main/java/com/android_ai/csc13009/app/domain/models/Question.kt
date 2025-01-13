package com.android_ai.csc13009.app.domain.models

import java.io.Serializable

class Question(
    var id : Int,
    var question : String,
    var type : String,
    var answer : ArrayList<AnswerWord>,

    // chon 1 trong 2
    var answers: List<Answer>,
    var isCorrect: Boolean?
): Serializable