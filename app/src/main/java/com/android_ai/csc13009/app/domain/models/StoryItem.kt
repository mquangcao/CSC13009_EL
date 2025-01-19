package com.android_ai.csc13009.app.domain.models

sealed class StoryItem {
    data class Narration(val text: String) : StoryItem()
    data class Message(val text: String, val gender : String) : StoryItem()
}