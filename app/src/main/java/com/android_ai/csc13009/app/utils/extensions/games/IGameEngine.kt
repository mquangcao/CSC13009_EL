package com.android_ai.csc13009.app.utils.extensions.games;

import com.android_ai.csc13009.app.data.local.dao.GameDataDao
import com.android_ai.csc13009.app.data.local.dao.WordDao
import com.android_ai.csc13009.app.data.local.entity.GameDataEntity
import com.android_ai.csc13009.app.data.local.entity.WordEntity;
import java.io.Serializable

import java.util.ArrayList;

public interface IGameEngine : Serializable {
    var score: Int;
    var highScore: Int;
    val words: ArrayList<WordEntity>;
    var currentWord: WordEntity?;
    val wordDao: WordDao;
    val gameDataDao: GameDataDao;



    // default functions
    suspend fun fetchHighScore() {
        val gameDate = gameDataDao.getGameDataByName(getGameName());
        if (gameDate == null) {
            gameDataDao.insertGameData(GameDataEntity(gameName = getGameName(), highScore = 0));
            highScore = 0;
        } else {
            highScore = gameDate.highScore;
        }
    };

    suspend fun fetchWord() {
        val word = wordDao.getRandomWord();
        if (word != null) {
            words.add(word);
        } else {
            val defaultWords = WordEntity(
                999,
                "default word",
                "default word meaning",
                "default word audio")
            words.add(defaultWords);
        }
    }

    suspend fun updateHighScore() {
        if (score > highScore) {
            highScore = score;
            gameDataDao.updateHighScore(getGameName(), highScore);
        }
    };

    abstract suspend fun startGame();
    abstract fun endGame();
    abstract fun submitAnswer   (answer: String);

    abstract fun nextRound();
    abstract fun getRule(): String;
    abstract fun getGameName(): String;
}
