package com.android_ai.csc13009.app.utils.extensions.games

import kotlin.random.Random

class StartLetterCondition : GameCondition {
    private var startLetter: Char = 'A'

    init {
        randomize()
    }

    override fun validate(answer: String): Boolean {
        return answer.uppercase().firstOrNull() == startLetter
    }

    override fun getConditionPrompt(): String {
        return "Find a word that starts with the letter $startLetter."
    }

    override fun randomize() {
        startLetter = Random.nextInt(65, 91).toChar() // Generates a random uppercase letter
    }
}