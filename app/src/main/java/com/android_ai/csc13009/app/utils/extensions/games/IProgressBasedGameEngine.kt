package com.android_ai.csc13009.app.utils.extensions.games;

public interface IProgressBasedGameEngine : IGameEngine {
    val maxRound: Int
    var currentRound: Int;

    override fun nextRound() {
        currentRound++;
        if (currentRound >= maxRound) {
            endGame();
        }

        currentWord = words[currentRound];
    }

    override suspend fun startGame() {
        for (i in 1..maxRound) {
            fetchWord()
        }
        nextRound()
    }
}
