package com.android_ai.csc13009.app.utils.extensions.games;

import com.android_ai.csc13009.app.data.local.dao.GameDataDao
import com.android_ai.csc13009.app.data.local.dao.WordDao
import com.android_ai.csc13009.app.data.local.entity.WordEntity;
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.Serializable

import kotlin.collections.ArrayList

public class SpellingBeeGameEngine(override val maxRound: Int,
                                   override val gameDataDao: GameDataDao,
                                   override val wordDao: WordDao
) :
    IProgressBasedGameEngine, Serializable {
    override var currentRound: Int = -1;
    override var score: Int = 0;
    override var highScore: Int = 0;
    override var currentWord: WordEntity? = null;
    override var streak: Int = 0;
    override val words: ArrayList<WordEntity> = ArrayList();
    override var gameState: IGameEngine.GameState = IGameEngine.GameState.WAITING;

    override fun submitAnswer(answer: String) {
        if (answer == currentWord?.word) {
            score += 1000;
            score += streak * 100;
            streak++;
            nextRound();
        } else {
            streak = 0;
            nextRound();
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
        return "Spelling Bee"
    }
}
