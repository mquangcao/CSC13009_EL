package com.android_ai.csc13009.app.data.remote.model

data class FirestoreLesson(
    val id: String,
    val lessonName: String,
    val topicId: String,
    val order : Int,
)