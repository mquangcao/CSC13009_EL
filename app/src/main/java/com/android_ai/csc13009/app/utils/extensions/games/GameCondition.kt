package com.android_ai.csc13009.app.utils.extensions.games

interface GameCondition {
    public abstract fun validate(answer: String): Boolean
    public abstract fun getConditionPrompt(): String
    public abstract fun randomize(): Unit
}