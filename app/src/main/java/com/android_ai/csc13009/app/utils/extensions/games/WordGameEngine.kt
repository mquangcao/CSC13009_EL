package com.android_ai.csc13009.app.utils.extensions.games;

import com.android_ai.csc13009.app.data.local.dao.WordDao
import com.android_ai.csc13009.app.data.local.entity.WordEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

public class WordGameEngine(override val maxRound: Int, override val dao: WordDao) :
    IProgressBasedGameEngine {
    override var currentRound: Int = 0;
    override var score: Int = 0;
    override var highScore: Int = 0;

    override val gameName: String = "Word Game";

    var index: Int = 0;
    var currentWord: WordEntity? = null;
    var streak: Int = 0;
    override val words: ArrayList<WordEntity> = ArrayList();

    override fun fetchHighScore() {
        highScore = 999
    }

    override fun startGame() {
        for (i in 1..maxRound) {
            fetchWord();
        }
        nextRound()
    }

    private fun fetchWord() {
        CoroutineScope(Dispatchers.IO).launch {
            val randomNumber = (1..100).random()
            dao.getWordById(randomNumber)?.let { words.add(it) }
        }
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
        currentRound++;
        if (currentRound >= maxRound) {
            endGame();
        }

        currentWord = words[index];
    }

    override fun getRule(): String {
        val rule: String =  "* This game is round-based, it will end after a number of round has passed \n" +
                            "* In each round, there will be an image representing a word \n" +
                            "* You need to drag and drop each letter into the correct position to form a word \n" +
                            "* A round will end when all positions have been filled, after that point will be added and a new word will be generated to start a new round \n" +
                            "* For each word you get correct, you will get 1000 points, and a bonus for each subsequent correct word \n" +
                            "* When the word is incorrect, the bonus is lost and you will not gain any point for that round \n";

        return rule;
    }

}
