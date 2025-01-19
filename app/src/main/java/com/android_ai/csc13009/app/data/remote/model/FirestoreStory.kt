package com.android_ai.csc13009.app.data.remote.model

data class FirestoreStory(
    var id : String,
    var storyName : String,
    var thumbnailUrl : String,
    var questions : List<FirestoreStoryQuestion> = emptyList(),
    var conversations: List<Conversation> = emptyList()) {
}