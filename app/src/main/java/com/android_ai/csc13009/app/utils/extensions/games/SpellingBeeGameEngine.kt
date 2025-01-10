package com.android_ai.csc13009.app.utils.extensions.games;

import com.android_ai.csc13009.app.data.local.dao.GameDataDao
import com.android_ai.csc13009.app.data.local.repository.WordRepository
import com.android_ai.csc13009.app.domain.repository.model.Word
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.Serializable

public class SpellingBeeGameEngine(
    override val wordRepository: WordRepository,
    override val gameDataDao: GameDataDao,
    override val maxRound: Int,

) :
    IProgressBasedGameEngine, Serializable {

    override var currentRound: Int = -1
    override var score: Int = 0;
    override var highScore: Int = 0;

    override var streak: Int = 0;
    override var gameState: IGameEngine.GameState = IGameEngine.GameState.WAITING;

    override val words: ArrayList<Word> = ArrayList();
    override var currentWord: Word? = null


    init {
        CoroutineScope(Dispatchers.IO).launch {
            fetchHighScore()
        }
    }

    override fun getRule(): String {
        val rule =  "* This game is round-based, which mean that the game will end after a number of rounds has ended \n" +
                    "* In each round, there will be a word which you need to spell out \n" +
                    "* You will be only provided with an audio and IPA of the word\n" +
                    "* You need to drag and drop each letter into the correct position to form the correct word \n" +
                    "* A round will end when all positions have been filled, after that point will be added and a new word will be generated to start a new round \n" +
                    "* For each word you get correct, you will get 1000 points, and a bonus for each subsequent correct word \n" +
                    "* When the word is incorrect, the bonus is lost and you will not gain any point for that round \n";

        return rule;
    }

    override fun getGameName(): String {
        return "Spelling bee";
    }

    override fun getProgress(): Int {
        return (currentRound + 1) * 100 / maxRound;
    }
}
