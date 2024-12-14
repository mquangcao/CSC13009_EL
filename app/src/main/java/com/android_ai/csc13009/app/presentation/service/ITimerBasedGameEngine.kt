package com.android_ai.csc13009.app.presentation.service;

import java.util.Timer;
import java.util.TimerTask;

public interface ITimerBasedGameEngine extends IGameEngine {
    int sessionDuration = 80; // Thời gian chơi game (giây)
    Timer timer = new Timer();

    default void startGame() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                endGame();
            }
        }, sessionDuration * 1000);
    }
}
