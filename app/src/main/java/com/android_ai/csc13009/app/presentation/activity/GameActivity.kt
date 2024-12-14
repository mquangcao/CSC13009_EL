package com.android_ai.csc13009.app.presentation.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.presentation.service.IGameEngine
import com.android_ai.csc13009.app.presentation.service.SpellingBeeGameEngine
import com.android_ai.csc13009.app.presentation.service.SynonymGameEngine
import com.android_ai.csc13009.app.presentation.service.WordGameEngine

class GameActivity : AppCompatActivity() {
    private var configMaxRound: Int = 5;
    private var configSessionDuration: Int = 60;

    public val gameEngines: List<IGameEngine> = listOf(
        SpellingBeeGameEngine(
            maxRound = configMaxRound
        ),
        SynonymGameEngine(
            sessionDuration = configSessionDuration
        ), WordGameEngine(
            maxRound = configMaxRound
        )
    );

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



    public fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.gamescreen_fcv, fragment)
            .commit()
    }

}