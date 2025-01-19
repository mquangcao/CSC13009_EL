package com.android_ai.csc13009.app.domain.models

data class ListeningTopics (var id: String,
                            var chapterName: String,
                            var totalLesson : Int,
                            var lessonFinished : Int,
                            var thumbnailUrl : String)