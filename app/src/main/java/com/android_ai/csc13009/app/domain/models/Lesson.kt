package com.android_ai.csc13009.app.domain.models

class Lesson(
    var id: Int,
    var lessonName: String,
    var totalQuestion : Int,
    var questionSuccess : Int,
    var totalWord : Int,
    var questions : List<Question>
) {
}