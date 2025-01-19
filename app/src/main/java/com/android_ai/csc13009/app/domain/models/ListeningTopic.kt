package com.android_ai.csc13009.app.domain.models

data class ListeningTopic (
    var id: String,
    var title: String,
    var totalLesson : Int,
    var lessonFinished : Int,
    var thumbnailUrl : String,
    var lessons : List<ListeningLesson>
)