package com.android_ai.csc13009.app.utils.extensions.games;

import com.android_ai.csc13009.app.data.local.dao.GameDataDao
import com.android_ai.csc13009.app.data.local.dao.WordDao
import com.android_ai.csc13009.app.data.local.entity.GameDataEntity
import com.android_ai.csc13009.app.data.local.entity.WordEntity;
import com.android_ai.csc13009.app.domain.repository.model.Word;
import com.android_ai.csc13009.app.data.local.repository.WordRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.Serializable

import java.util.ArrayList;

public interface IGameEngine : Serializable {
    var score: Int;
    var highScore: Int;
    val words: ArrayList<Word>;
    var currentWord: Word?;
//    val wordDao: WordDao;
    val wordRepository: WordRepository;
    val gameDataDao: GameDataDao;
    var streak: Int

    enum class GameState {
        WAITING,
        PLAYING,
        FINISHED
    }
    var gameState: GameState;



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
//        val word = wordDao.getRandomWord();
        val word = wordRepository.getRandomWord();
        if (word != null) {
            words.add(word);
        } else {
            val defaultWord = Word(
                -1,
                "default word",
                "",
                "")
            words.add(defaultWord);
        }
    }

    suspend fun updateHighScore() {
        if (score > highScore) {
            highScore = score;
            gameDataDao.updateHighScore(getGameName(), highScore);
        }
    };

    fun endGame() {
        gameState = GameState.FINISHED;
    };

    suspend fun startGame() {
        gameState = GameState.PLAYING;
    };

    abstract suspend fun submitAnswer   (answer: String);

    abstract fun nextRound();
    abstract fun getRule(): String;
    abstract fun getGameName(): String;
}
