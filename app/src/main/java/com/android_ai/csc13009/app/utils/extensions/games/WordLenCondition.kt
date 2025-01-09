package com.android_ai.csc13009.app.utils.extensions.games

import kotlin.random.Random

class WordLenCondition: GameCondition {
    var letterCount: Int = 0
    init {
        randomize()
    }

    override fun validate(answer: String): Boolean {
        return answer.length == letterCount;
    }

    override fun getConditionPrompt(): String {
        return "Find a word with $letterCount letters."
    }
    override fun randomize() {
        letterCount = Random.nextInt(4, 9) // Generates a random number between 4 (inclusive) and 9 (exclusive)
    }

}