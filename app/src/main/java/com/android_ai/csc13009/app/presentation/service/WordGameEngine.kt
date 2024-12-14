package com.android_ai.csc13009.app.presentation.service;

import com.android_ai.csc13009.app.data.local.entity.WordEntity

public class WordGameEngine(override val maxRound: Int) : IProgressBasedGameEngine {
    override var currentRound: Int = 0;
    override var score: Int = 0;
    override var highScore: Int = 0;

    override val gameName: String = "Word Game";

    val rule: String = "Rule";
    var index: Int = 0;
    var currentWord: WordEntity? = null;
    var streak: Int = 0;
    override val words: ArrayList<WordEntity> = ArrayList();

    override fun fetchHighScore() {
        TODO("Not yet implemented")
    }

    override fun startGame() {
        TODO("Not yet implemented")
    }

    override fun endGame() {
        TODO("Not yet implemented")
    }

    override fun submitAnswer(answer: String) {
        TODO("Not yet implemented")
    }

    override fun updateHighScore() {
        TODO("Not yet implemented")
    }

    override fun nextRound() {
        TODO("Not yet implemented")
    }

    override fun getRule(): String {
        TODO("Not yet implemented")
    }

}
