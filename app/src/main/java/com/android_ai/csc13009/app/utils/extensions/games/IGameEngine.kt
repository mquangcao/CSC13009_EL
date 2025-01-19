package com.android_ai.csc13009.app.utils.extensions.games

import android.content.Context
import com.android_ai.csc13009.app.data.local.dao.GameDataDao
import com.android_ai.csc13009.app.data.local.entity.GameDataEntity
import com.android_ai.csc13009.app.data.repository.WordRepository
import com.android_ai.csc13009.app.domain.models.Word
import com.android_ai.csc13009.app.domain.models.WordModel


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.Serializable

interface IGameEngine : Serializable {
    var score: Int
    var highScore: Int
    val words: ArrayList<WordModel>
    var currentWord: WordModel?
    val wordRepository: WordRepository
    val gameDataDao: GameDataDao
    var streak: Int
    val context: Context

    var startTime: Long
    var elapsedTime: Long
    var bonusScore: Int
    var correctAnswerCount: Int

    enum class GameState {
        WAITING,
        PLAYING,
        FINISHED
    }
    var gameState: GameState

    // default functions
    suspend fun fetchHighScore() {
        val gameDate = gameDataDao.getGameDataByName(getGameName())
        if (gameDate == null) {
            gameDataDao.insertGameData(GameDataEntity(gameName = getGameName(), highScore = 0))
            highScore = 0
        } else {
            highScore = gameDate.highScore
        }
    }

    suspend fun fetchWord() {
        val word = wordRepository.getRandomWord()
        while (word == null || words.contains(word) || word.word.length > 12) {
            wordRepository.getRandomWord()
        }

        words.add(word)

//        if (word != null) {
//            words.add(word)
//        } else {
//            val defaultWord = WordModel(
//                -1,
//                "default word",
//                "",
//                "")
//            words.add(defaultWord)
//        }
    }

    suspend fun fetchWords(count: Int) {
        val wordList = wordRepository.getRandomWords(count)
        words.addAll(wordList)
    }

    suspend fun updateHighScore() {
        bonusScore = correctAnswerCount * 500
        bonusScore -= (elapsedTime / 100).toInt()
        if (bonusScore < 0) {
            bonusScore = 0
        }
        score += bonusScore
        if (score > highScore) {
            highScore = score
            gameDataDao.updateHighScore(getGameName(), highScore)
        }
    }

    fun endGame() {
        gameState = GameState.FINISHED
        elapsedTime = System.currentTimeMillis() - startTime
        CoroutineScope(Dispatchers.IO).launch {
            updateHighScore()
        }
    }

    suspend fun startGame() {
        gameState = GameState.PLAYING
        startTime = System.currentTimeMillis()
    }

    suspend fun submitAnswer(answer: String) : Boolean

    fun nextRound()
    fun getRule(): String
    fun getGameName(): String
    fun getProgress(): Int
}
