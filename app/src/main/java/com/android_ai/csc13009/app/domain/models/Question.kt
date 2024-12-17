package com.android_ai.csc13009.app.domain.models

class Question(
    var id : Int,
    var question : String,
    var type : String,
    var answer : List<Answer>
) {

}