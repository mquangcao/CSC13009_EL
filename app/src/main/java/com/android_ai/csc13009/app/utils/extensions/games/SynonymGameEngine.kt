package com.android_ai.csc13009.app.utils.extensions.games;

import com.android_ai.csc13009.app.data.local.dao.GameDataDao
import com.android_ai.csc13009.app.data.local.repository.WordRepository
import com.android_ai.csc13009.app.domain.repository.model.Word
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.Serializable

public class SynonymGameEngine(
//    override val wordDao: WordDao,
    override val wordRepository: WordRepository,
    override val gameDataDao: GameDataDao,
    override val maxRound: Int,
    override var currentRound: Int
) :
    IProgressBasedGameEngine, Serializable {
    override var score: Int = 0;
    override var highScore: Int = 0;

    override var streak: Int = 0;
    override var gameState: IGameEngine.GameState = IGameEngine.GameState.WAITING;

    //    var index: Int = 0;
    val currentWordAnswers = ArrayList<String>();

    override val words: ArrayList<Word> = ArrayList();
    override var currentWord: Word? = null


    init {
        CoroutineScope(Dispatchers.IO).launch {
            fetchHighScore()
        }
    }


    override suspend fun submitAnswer(answer: String) {

        if (answer in currentWordAnswers) {
            score += 1000;
            score += streak * 100;
            streak++;
            currentWordAnswers.remove(answer)
            if (currentWordAnswers.isEmpty()) {
                nextRound();
            }
        } else {
            streak = 0;
        }
    }


    override fun getRule(): String {
        val rule =  "* This game is time-based, which mean that the game will end after a set of time has ended \n" +
                    "* In each round, there will be a word \n" +
                    "* You need to type out the words that are synonyms of that featured words \n" +
                    "* A round will end when all of the synonyms of that word are used, after that a new word will replace the current featured word \n" +
                    "* For each correct synonym, you will gain 1000 points, and an extra bonus for subsequent right answers" +
                    "* The same correct answer cannot be used twice for each round, the previous answers will be shown in a list \n" +
                    "* If the word is incorrect, the bonus will be lost and you will not gain any point for that answer"

        return rule;
    }

    override fun getGameName(): String {
        return "Synonym";
    }
}
