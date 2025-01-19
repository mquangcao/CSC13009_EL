package com.android_ai.csc13009.app.data.remote.model

data class FirestoreStoryQuestion(
    var id: String = "",
    var storyId : String = "",
    var question: String = "",
    var type: String = "",
    var answers: List<FirestoreAnswers> = emptyList()
) {

}