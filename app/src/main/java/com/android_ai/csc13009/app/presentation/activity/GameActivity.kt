package com.android_ai.csc13009.app.presentation.activity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.data.local.AppDatabase
import com.android_ai.csc13009.app.data.local.repository.WordRepository
import com.android_ai.csc13009.app.presentation.fragment.games.GameInterface
import com.android_ai.csc13009.app.utils.extensions.games.IGameEngine
import com.android_ai.csc13009.app.utils.extensions.games.LexiconGameEngine
import com.android_ai.csc13009.app.utils.extensions.games.SynonymGameEngine
import com.android_ai.csc13009.app.utils.extensions.games.WordGameEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.Serializable


@SuppressLint("NewApi")
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

    public fun submitAnswer(answer: String) {

//        gameEngine?.submitAnswer(answer)
//        val currentFragment = supportFragmentManager.findFragmentById(R.id.gamescreen_fcv)
//        if (currentFragment is GameInterface)  {
//            currentFragment.nextRound()
//        }
        CoroutineScope(Dispatchers.Main).launch {
            gameEngine?.submitAnswer(answer) // Ensure this completes first

            val currentFragment = supportFragmentManager.findFragmentById(R.id.gamescreen_fcv)
            if (currentFragment is GameInterface) {
                currentFragment.nextRound() // Now, this will only execute after `submitAnswer`
            }
        }
    }

    public fun nextRound() {
        gameEngine?.nextRound()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun createGameEngine(): IGameEngine? {
        val passedData = intent.getIntExtra("passedData", 1)

        val dataDao = database.gameDataDao()
        val wordDao = database.wordDao()
        val wordRepository = WordRepository(wordDao);
        return when (passedData) {
            0 -> LexiconGameEngine(
                maxRound = 5,
                gameDataDao = dataDao,
                wordRepository = wordRepository
            )
            2 -> WordGameEngine(
                maxRound = 5,
                gameDataDao = dataDao,
                wordRepository = wordRepository
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