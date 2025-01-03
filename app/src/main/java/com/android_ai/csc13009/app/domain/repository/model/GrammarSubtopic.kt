package com.android_ai.csc13009.app.domain.repository.model


data class GrammarSubtopic(
    val id: Int,
    val topicId: Int,
    val name: String,
    val content: String,
    val structures: String,
    val examples: String
)