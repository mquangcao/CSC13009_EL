package com.android_ai.csc13009.app.domain.models

data class Lesson(
    var id: Int,
    var lessonName: String,
    var totalQuestion : Int,
//    var questionSuccess : Int,
    var questions : List<Question>
)