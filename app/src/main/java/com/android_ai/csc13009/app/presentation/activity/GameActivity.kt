package com.android_ai.csc13009.app.presentation.activity

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.data.local.AppDatabase
import com.android_ai.csc13009.app.utils.extensions.games.IGameEngine
import com.android_ai.csc13009.app.utils.extensions.games.SpellingBeeGameEngine
import com.android_ai.csc13009.app.utils.extensions.games.SynonymGameEngine
import com.android_ai.csc13009.app.utils.extensions.games.WordGameEngine
import java.io.Serializable


class GameActivity : AppCompatActivity() {
    private val database : AppDatabase by lazy { AppDatabase.getInstance(this) }

    val gameEngine: IGameEngine? by lazy {
        createGameEngine()
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun createGameEngine(): IGameEngine? {
        val passedData = intent.getIntExtra("passedData", 1)

        val dataDao = database.gameDataDao()
        val wordDao = database.wordDao()

        return when (passedData) {
            0 -> SpellingBeeGameEngine(
                maxRound = 5,
                gameDataDao = dataDao,
                wordDao = wordDao
            )
            1 -> SynonymGameEngine(
                sessionDuration = 60,
                gameDataDao = dataDao,
                wordDao = wordDao
            )
            2 -> WordGameEngine(
                maxRound = 5,
                gameDataDao = dataDao,
                wordDao = wordDao
            )
            else -> null // Handle invalid cases gracefully
        }
    }

    public fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.gamescreen_fcv, fragment)
            .commit()
    }

}