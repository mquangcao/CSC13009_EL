package com.android_ai.csc13009.app.utils.extensions.games

import android.content.Context
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.data.local.dao.GameDataDao
import com.android_ai.csc13009.app.data.repository.WordRepository
import com.android_ai.csc13009.app.domain.models.WordModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.Serializable

class SpellingBeeGameEngine(
    override val wordRepository: WordRepository,
    override val gameDataDao: GameDataDao,
    override val maxRound: Int,
    override val context: Context
    ) :
    IProgressBasedGameEngine, Serializable {

    override var currentRound: Int = -1
    override var score: Int = 0
    override var highScore: Int = 0

    override var streak: Int = 0
    override var gameState: IGameEngine.GameState = IGameEngine.GameState.WAITING

    override val words: ArrayList<WordModel> = ArrayList()
    override var currentWord: WordModel? = null


    init {
        CoroutineScope(Dispatchers.IO).launch {
            fetchHighScore()
        }
    }

    override fun getRule(): String {
        val rule = context.getString(R.string.game_rule_spelling_bee)
        return rule
    }

    override fun getGameName(): String {
        val name = context.getString(R.string.game_name_spelling_bee)
        return "Spelling bee"
    }

    override fun getProgress(): Int {
        return (currentRound + 1) * 100 / maxRound
    }
}
