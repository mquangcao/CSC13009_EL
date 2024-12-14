package com.android_ai.csc13009.app.presentation.service;

import android.os.CountDownTimer
import java.util.Timer;
import java.util.TimerTask;

public interface ITimerBasedGameEngine : IGameEngine {
    var sessionDuration: Int; // Thời gian chơi game (giây)
    var timer: CountDownTimer;

}