package com.android_ai.csc13009.app.utils.extensions.games

import android.content.Context
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.data.local.dao.GameDataDao
import com.android_ai.csc13009.app.data.repository.WordRepository
import com.android_ai.csc13009.app.domain.models.Word
import com.android_ai.csc13009.app.domain.models.WordModel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.Serializable

class WordGameEngine(
    override val maxRound: Int,
    override val wordRepository: WordRepository,
    override val gameDataDao: GameDataDao,
    override val context: Context
) :
    IProgressBasedGameEngine, Serializable {
    override var currentRound: Int = -1
    override var score: Int = 0
    override var highScore: Int = 0
    override var startTime: Long = 0
    override var elapsedTime: Long = 0
    override var bonusScore: Int = 5000
    override var correctAnswerCount: Int = 0

    override var currentWord: WordModel? = null
    override var streak: Int = 0
    override var gameState: IGameEngine.GameState = IGameEngine.GameState.WAITING
    override val words: ArrayList<WordModel> = ArrayList()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            fetchHighScore()
        }
    }



    override fun getRule(): String {
        val rule = context.getString(R.string.game_rule_unscramble)
        return rule
    }

    override fun getGameName(): String {
        val name = context.getString(R.string.game_name_unscramble)
        return name
    }

    override fun getProgress(): Int {
        return (currentRound + 1) * 100 / maxRound
    }

}
