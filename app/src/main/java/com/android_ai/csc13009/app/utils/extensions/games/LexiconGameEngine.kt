package com.android_ai.csc13009.app.utils.extensions.games
import com.android_ai.csc13009.app.data.local.dao.GameDataDao
import com.android_ai.csc13009.app.data.repository.WordRepository
import com.android_ai.csc13009.app.domain.models.Word
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
) :

    IGameEngine, Serializable {
    override var score: Int = 0
    override var highScore: Int = 0
    override var currentWord: WordModel? = null
    override var streak: Int = 0
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

    override suspend fun submitAnswer(answer: String) {
        val currentWord = findWord(answer)
        if (currentWord != null && !words.contains(currentWord)) {


            val answerLength = answer.length
            var answerScore = 100 * answerLength
            if (currentCondition?.validate(answer) == true) {
                answerScore *= 2
            }
            score += answerScore
            words.add(currentWord)
            nextRound()
        } else {
            score -= 100
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
        val rule: String =
                "* This game is input-based, it will end after a number correct answer is entered \n" +
                "* You will need to input x unique word in order to complete the game \n" +
                "* The point you gain will based on the length of the word you entered \n" +
                "* An extra random condition appear for each new input, fulfil them to double the point of the word \n" +
                "* When the word is incorrect / not found in our codex, you get deduced 100 points and have to try again \n"

        return rule
    }

    override fun getGameName(): String {
        return "Lexicon"
    }

    override fun getProgress(): Int {
        return (words.size + 1) * 100 / maxRound
    }

    override suspend fun startGame() {
        super.startGame()
        getNewCondition()
    }
}
