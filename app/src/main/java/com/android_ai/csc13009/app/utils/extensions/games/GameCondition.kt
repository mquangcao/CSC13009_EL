package com.android_ai.csc13009.app.utils.extensions.games

interface GameCondition {
    fun validate(answer: String): Boolean
    fun getConditionPrompt(): String
    fun randomize()
}