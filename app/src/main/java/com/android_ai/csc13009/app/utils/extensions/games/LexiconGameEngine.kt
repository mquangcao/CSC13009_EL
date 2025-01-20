package com.android_ai.csc13009.app.utils.extensions.games
import android.content.Context
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.data.local.dao.GameDataDao
import com.android_ai.csc13009.app.data.repository.WordRepository
import com.android_ai.csc13009.app.domain.models.WordModel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.Serializable

import kotlin.collections.ArrayList

class LexiconGameEngine(
    val maxRound: Int,
    override val gameDataDao: GameDataDao,
    override val wordRepository: WordRepository,
    override val context: Context
) :

    IGameEngine, Serializable {
    override var score: Int = 0
    override var highScore: Int = 0
    override var currentWord: WordModel? = null
    override var streak: Int = 0
    override var startTime: Long = 0
    override var elapsedTime: Long = 0
    override var bonusScore: Int = 5000
    override var correctAnswerCount: Int = 0
    override val words: ArrayList<WordModel> = ArrayList()
    override var gameState: IGameEngine.GameState = IGameEngine.GameState.WAITING

    private val gameConditions = listOf(
        WordLenCondition(),
        LetterCountCondition(),
        StartLetterCondition()
    )
    var currentCondition: GameCondition? = null

    init {
        CoroutineScope(Dispatchers.IO).launch {
            fetchHighScore()
        }
    }

    override suspend fun submitAnswer(answer: String): Boolean {
        val currentWord = findWord(answer)
        return if (currentWord != null && !words.contains(currentWord)) {


            val answerLength = answer.length
            var answerScore = 100 * answerLength
            if (currentCondition?.validate(answer) == true) {
                answerScore *= 2
            }
            score += answerScore
            words.add(currentWord)
            correctAnswerCount++
            nextRound()

            true
        } else {
            score -= 100
            false
        }
    }

    override fun nextRound() {
        if (words.size >= maxRound) {
            endGame()
            return
        }

        // get random GameCondition subclass
        getNewCondition()
    }

    private fun getNewCondition() {
        currentCondition = gameConditions.random()
        currentCondition?.randomize()
    }

    private suspend fun findWord(word: String): WordModel? {
        return withContext(Dispatchers.IO) {
            wordRepository.getExactWordByName(word)
        }
    }

    override fun getRule(): String {
        val rule = context.getString(R.string.game_rule_lexicon)
        return rule
    }

    override fun getGameName(): String {
        val name = context.getString(R.string.game_name_lexicon)
        return name
    }

    override fun getProgress(): Int {
        return (words.size + 1) * 100 / maxRound
    }

    override suspend fun startGame() {
        super.startGame()
        getNewCondition()
    }
}
