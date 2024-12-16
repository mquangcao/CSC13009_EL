package com.android_ai.csc13009.app.domain.models

class Chapter(
    var id: Int,
    var title: String,
    var totalLesson : Int,
    var lessonFinished : Int,
    var totalWord : Int,
    var thumbnailUrl : String,
    var lessons : List<Lesson>
)