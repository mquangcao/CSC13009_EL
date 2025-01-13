package com.android_ai.csc13009.app.domain.models;

data class GrammarQuestion(
        val grammarQuestionId: Int,
        val grammarTopicId: Int,
        val name: String,
        val type: String
)