package com.android_ai.csc13009.app.utils.extensions.games

import kotlin.random.Random

class LetterCountCondition : GameCondition {
    private var letterCount: Int = 0
    private var letter: Char = 'A'
    init {
        randomize()
    }

    override fun validate(answer: String): Boolean {
        val upperAns = answer.uppercase()
        val answerLetterCount = upperAns.count { it == letter }
        return letterCount == answerLetterCount
    }

    override fun getConditionPrompt(): String {
        return "Find a word with $letterCount $letter(s)."
    }

    override fun randomize() {
        letterCount = Random.nextInt(1, 2) // Generates a random number between 4 (inclusive) and 9 (exclusive)
        letter = Random.nextInt(65, 91).toChar() // Generates a random uppercase letter
    }
}