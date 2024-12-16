package com.android_ai.csc13009.app.presentation.service;

import android.os.CountDownTimer
import com.android_ai.csc13009.app.data.local.entity.WordEntity

public class SynonymGameEngine(override var sessionDuration: Int) : ITimerBasedGameEngine{
    override var score: Int = 0;
    override var highScore: Int = 0;

    override val gameName: String = "Synonym";

    var secLeft = sessionDuration;
    var timeLeft = sessionDuration * 1000L;

    var streak: Int = 0;
    var index: Int = 0;
    var currentWord: WordEntity? = null;
    val currentWordAnswers = ArrayList<String>();

    override val words: ArrayList<WordEntity> = ArrayList();

    override lateinit var timer: CountDownTimer;

    fun timerStart(timeLength: Long) {
        val timer = object : CountDownTimer(timeLength, 1000) {

            override fun onTick(p0: Long) {
                timeLeft = p0;
                secLeft = (p0 / 1000).toInt()
            }
            override fun onFinish() {
                endGame();
            }

        }

        timer.start()
    }

    fun timerPause() {
        timer.cancel()
    }

    fun timerResume() {
        timerStart(timeLeft)
    }


    override fun fetchHighScore() {
        highScore = 999
    }

    override fun startGame() {
        fetchWord()
        timer.start()
    }

    fun fetchWord() {
        words.add(WordEntity(1, "word1", "meaning", "a", "a", null, null ))
    }

    override fun endGame() {
        TODO("Not yet implemented")
    }

    override fun submitAnswer(answer: String) {
        timerPause()

        if (answer in currentWordAnswers) {
            score += 1000;
            score += streak * 100;
            streak++;
            currentWordAnswers.remove(answer)
            if (currentWordAnswers.isEmpty()) {
                nextRound();
            }
        } else {
            streak = 0;
        }
        timerResume();

    }

    override fun updateHighScore() {
        highScore = 9999
    }

    override fun nextRound() {
        fetchWord();
    }

    override fun getRule(): String {
        return "Rule";
    }
}
