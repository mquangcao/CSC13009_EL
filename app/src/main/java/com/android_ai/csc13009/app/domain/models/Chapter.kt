package com.android_ai.csc13009.app.domain.models

class Chapter(
    var id: String,
    var title: String,
    var totalLesson : Int,
    var lessonFinished : Int,
    var thumbnailUrl : String,
    var lessons : List<Lesson>
)