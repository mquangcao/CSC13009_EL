package com.android_ai.csc13009.app.domain.models

data class Lesson(
    var id: String,
    var lessonName: String,
    var totalQuestion : Int,
    var questionSuccess : Int,
    var order : Int,
    var questions : List<Question>,
    var isOpen : Boolean,
    var isOpenByProgress : Boolean
)