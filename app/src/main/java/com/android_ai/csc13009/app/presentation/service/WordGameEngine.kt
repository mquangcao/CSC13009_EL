package com.android_ai.csc13009.app.presentation.service;

import com.android_ai.csc13009.app.data.local.entity.WordEntity

public class WordGameEngine(override val maxRound: Int) : IProgressBasedGameEngine {
    override var currentRound: Int = 0;
    override var score: Int = 0;
    override var highScore: Int = 0;

    override val gameName: String = "Word Game";

    private val rule: String = "Rule";
    var index: Int = 0;
    var currentWord: WordEntity? = null;
    var streak: Int = 0;
    override val words: ArrayList<WordEntity> = ArrayList();

    override fun fetchHighScore() {
        highScore = 999
    }

    override fun startGame() {
        TODO("Not yet implemented")
    }

    override fun endGame() {
        updateHighScore()
    }

    override fun submitAnswer(answer: String) {
        if (answer == currentWord?.word) {
            score += 1000;
            score += streak * 100;
            streak++;
            nextRound();
        } else {
            streak = 0;
            nextRound();
        }
    }

    override fun updateHighScore() {
        highScore = 9999
    }

    override fun nextRound() {
        TODO("Not yet implemented")
    }

    override fun getRule(): String {
        return rule;
    }

}
