package com.android_ai.csc13009.app.presentation.service;

import com.android_ai.csc13009.app.data.local.AppDatabase
import com.android_ai.csc13009.app.data.local.dao.WordDao
import com.android_ai.csc13009.app.data.local.entity.WordEntity;
import java.io.Serializable

import java.util.ArrayList;

public interface IGameEngine : Serializable {
    var score: Int;
    var highScore: Int;
    val words: ArrayList<WordEntity>;
    val dao: WordDao;

    val gameName: String;



    abstract fun fetchHighScore();
    abstract fun startGame();
    abstract fun endGame();
    abstract fun submitAnswer   (answer: String);
    abstract fun updateHighScore();
    abstract fun nextRound();
    abstract fun getRule(): String;
}
