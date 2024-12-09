package com.android_ai.csc13009.app.presentation.service;

public interface IGameEngine {

    int currentScore = 0;
    int highScore = 0;

    abstract void fetchHighScore();
    abstract void startGame();
    abstract void endGame();
    abstract void submitAnswer(String answer);
    abstract void updateScore(int points);
    abstract void updateProgress(int progress);
    abstract void updateHighScore(int score);
    abstract void nextRound();
}
