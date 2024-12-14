package com.android_ai.csc13009.app.presentation.service;

public interface IProgressBasedGameEngine : IGameEngine {
    val maxRound: Int
    var currentRound: Int
}
