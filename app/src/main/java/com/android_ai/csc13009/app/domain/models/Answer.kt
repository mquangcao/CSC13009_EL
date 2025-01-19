package com.android_ai.csc13009.app.domain.models;

import com.android_ai.csc13009.app.domain.models.Word

data class Answer (
    var answerWord: Word,
    var id : String,
    var answer : String,
    var isCorrect : Boolean,
    var thumbNails : String
) {


}