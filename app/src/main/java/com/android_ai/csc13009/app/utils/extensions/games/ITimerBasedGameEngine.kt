package com.android_ai.csc13009.app.utils.extensions.games;

import android.os.CountDownTimer

public interface ITimerBasedGameEngine : IGameEngine {
    var sessionDuration: Int; // Thời gian chơi game (giây)
    var timer: CountDownTimer;

}