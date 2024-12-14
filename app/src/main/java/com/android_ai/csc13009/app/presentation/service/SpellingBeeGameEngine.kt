package com.android_ai.csc13009.app.presentation.service;

import com.android_ai.csc13009.app.data.local.entity.WordEntity;

import kotlin.collections.ArrayList

public class SpellingBeeGameEngine(override val maxRound: Int) : IProgressBasedGameEngine {
    override var currentRound: Int = 0;
    override var score: Int = 0;
    override var highScore: Int = 0;

    override val gameName: String = "Spelling Bee";

    val rule: String = "Rule";

    override val words: ArrayList<WordEntity> = ArrayList();
    var index: Int = 0;
    var currentWord: WordEntity? = null;
    var streak: Int = 0;

    override fun fetchHighScore() {
        TODO("Not yet implemented")
        // Lấy điểm cao nhất từ nguồn dữ liệu (ví dụ: cơ sở dữ liệu)

    }

    override fun startGame() {
        for (i in 1..maxRound) {
            fetchWord();
        }
        nextRound()
    }

    private fun fetchWord() {
        // Lấy từ từ nguồn dữ liệu (ví dụ: từ điển)
//        ArrayList
//        words.add()
    };

    override fun endGame() {

        updateHighScore();
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
        if (score > highScore) {

        }
    }

    override fun nextRound() {
        currentRound++;
        if (currentRound >= maxRound) {
            endGame();
        }

        currentWord = words[index];
    }

    override fun getRule(): String {
        return rule;
    }
}
