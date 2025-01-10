package com.android_ai.csc13009.app.utils.extensions.games;

import android.os.CountDownTimer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

public interface ITimerBasedGameEngine : IGameEngine {
    var sessionDuration: Int; // Thời gian chơi game (giây)
    var timer: CountDownTimer;

    override fun nextRound() {
        CoroutineScope(Dispatchers.IO).launch {
            fetchWord()
        }
        currentWord = words.lastOrNull()
    }

    override suspend fun startGame() {
        fetchWord()
        super.startGame()
        timer.start()
    }

}