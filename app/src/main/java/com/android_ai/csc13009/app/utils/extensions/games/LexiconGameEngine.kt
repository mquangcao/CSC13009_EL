package com.android_ai.csc13009.app.utils.extensions.games;

import com.android_ai.csc13009.app.data.local.dao.GameDataDao
import com.android_ai.csc13009.app.data.local.dao.WordDao
import com.android_ai.csc13009.app.data.local.entity.WordEntity;
import com.android_ai.csc13009.app.data.local.repository.WordRepository
import com.android_ai.csc13009.app.domain.repository.model.Word
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.Serializable

import kotlin.collections.ArrayList

public class LexiconGameEngine(
    val maxRound: Int,
    override val gameDataDao: GameDataDao,
    override val wordRepository: WordRepository,
) :
    IGameEngine, Serializable {
    override var score: Int = 0;
    override var highScore: Int = 0;
//    override var currentWord: WordEntity? = null;
    override var currentWord: Word? = null
    override var streak: Int = 0;
    override val words: ArrayList<Word> = ArrayList();
    override var gameState: IGameEngine.GameState = IGameEngine.GameState.WAITING;

    private val gameConditions = listOf(
        WordLenCondition(),
        LetterCountCondition(),
        StartLetterCondition()
    )
    var currentCondition: GameCondition? = null;

    override suspend fun submitAnswer(answer: String) {
        val currentWord = findWord(answer)
        if (currentWord != null) {
            val answerLength = answer.length;
            var answerScore = 100 * answerLength
            if (currentCondition?.validate(answer) == true) {
                answerScore *= 2;
            }
            score += answerScore;
            words.add(currentWord);
            nextRound();
        } else {
            score -= 10;
        }
    }

    override fun nextRound() {
        if (words.size >= maxRound) {
            endGame();
            return;
        }

        // get random GameCondition subclass
        getNewCondition();
    }

    private fun getNewCondition() {
        currentCondition = gameConditions.random();
        currentCondition?.randomize()
    }

    private suspend fun findWord(word: String): Word? {
        return withContext(Dispatchers.IO) {
            wordRepository.getExactWordByName(word)
        }
    }

    override fun getRule(): String {
        val rule: String =  "* This game is round-based, it will end after a number of round has passed \n" +
                "* In each round, there will be an audio clip of a word \n" +
                "* You need to drag and drop each letter into the correct position to form a word \n" +
                "* A round will end when all positions have been filled, after that point will added and a new word will be generated to start a new round \n" +
                "* For each word you get correct, you will get 1000 points, and a bonus for each subsequent correct word \n" +
                "* When the word is incorrect, the bonus is lost and you will not gain any point for that round \n"
        ;
        return rule;
    }

    override fun getGameName(): String {
        return "Lexicon"
    }

    override suspend fun startGame() {
        super.startGame();
        getNewCondition();
    }
}
