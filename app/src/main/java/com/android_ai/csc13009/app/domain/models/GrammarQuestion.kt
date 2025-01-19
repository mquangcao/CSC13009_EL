package com.android_ai.csc13009.app.domain.models;

import java.io.Serializable

data class GrammarQuestion(
        val id: String,
        val grammarTopicId: String,
        val name: String,
        val type: String
): Serializable