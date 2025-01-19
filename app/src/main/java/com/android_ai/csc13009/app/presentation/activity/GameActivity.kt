package com.android_ai.csc13009.app.presentation.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.data.local.AppDatabase
import com.android_ai.csc13009.app.data.repository.WordRepository
import com.android_ai.csc13009.app.domain.models.WordModel
import com.android_ai.csc13009.app.presentation.fragment.games.GameInterface
import com.android_ai.csc13009.app.utils.extensions.NavigationSetter
import com.android_ai.csc13009.app.utils.extensions.games.IGameEngine
import com.android_ai.csc13009.app.utils.extensions.games.LexiconGameEngine
import com.android_ai.csc13009.app.utils.extensions.games.SpellingBeeGameEngine
import com.android_ai.csc13009.app.utils.extensions.games.WordGameEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("NewApi")
class GameActivity : AppCompatActivity() {
    private val database : AppDatabase by lazy { AppDatabase.getInstance(this) }
    private lateinit var dialog: Dialog
    private lateinit var dialogInCorrect: Dialog
    private lateinit var dialogIncorrectAnswerCorrection: TextView
    private lateinit var btnConfirmDialog: Button
    private lateinit var btnConfirmDialogIncorrect: Button

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
        NavigationSetter.setActivityBackConfirmation(this)

        dialog = Dialog(this)
        dialog.setContentView(R.layout.custom_dialog_answer_correct)
        dialog.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawableResource(R.drawable.custom_dialog_bg)
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.setCancelable(false)

        dialogInCorrect = Dialog(this)
        dialogInCorrect.setContentView(R.layout.custom_dialog_answer_uncorrect)
        dialogInCorrect.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogInCorrect.window?.setBackgroundDrawableResource(R.drawable.custom_dialog_bg)
        dialogInCorrect.window?.setGravity(Gravity.BOTTOM)
        dialogInCorrect.setCancelable(false)
        dialogIncorrectAnswerCorrection = dialogInCorrect.findViewById(R.id.tv_content)

        btnConfirmDialog = dialog.findViewById(R.id.btn_continue)
        btnConfirmDialogIncorrect = dialogInCorrect.findViewById(R.id.btn_continue)


        btnConfirmDialog.setOnClickListener {
            dialog.dismiss()
            showNextRound()
        }

        btnConfirmDialogIncorrect.setOnClickListener {
            dialogInCorrect.dismiss()
            showNextRound()
        }

    }

    fun showLoading() {
        val loadingProgressBar = findViewById<ProgressBar>(R.id.game_loading_pb)
        loadingProgressBar.visibility = View.VISIBLE
    }

    fun hideLoading() {
        val loadingProgressBar = findViewById<ProgressBar>(R.id.game_loading_pb)
        loadingProgressBar.visibility = View.GONE
    }

    fun submitAnswer(answer: String) {
        if (gameEngine is LexiconGameEngine) {
            submitWithoutDialog(answer)
        } else {
            submitWithDialog(answer)
        }
    }

    fun submitWithDialog(answer: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val correctAnswer = gameEngine?.currentWord?.word ?: ""
            val result = gameEngine?.submitAnswer(answer) // Ensure this completes first
            if (result == true) {
                dialog.show()
            } else {
                dialogIncorrectAnswerCorrection.text = correctAnswer
                dialogInCorrect.show()
            }
        }
    }

    fun submitWithoutDialog(answer: String) {
        CoroutineScope(Dispatchers.Main).launch {
            gameEngine?.submitAnswer(answer)
            showNextRound()
        }
    }

    fun showNextRound() {
        val currentFragment = getCurrentFragment()
        if (currentFragment is GameInterface) {
            currentFragment.nextRound()
        }
    }

    private fun getCurrentFragment(): Fragment? {
        return supportFragmentManager.findFragmentById(R.id.gamescreen_fcv)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun createGameEngine(): IGameEngine? {
        val passedData = intent.getIntExtra("passedData", 1)

        val dataDao = database.gameDataDao()
        val wordDao = database.wordDao()
        val wordRepository = WordRepository(wordDao)
        return when (passedData) {
            0 -> LexiconGameEngine(
                maxRound = 5,
                gameDataDao = dataDao,
                wordRepository = wordRepository,
                this
            )
            1 -> SpellingBeeGameEngine(
                maxRound = 5,
                gameDataDao = dataDao,
                wordRepository = wordRepository,
                context = this
            )
            2 -> WordGameEngine(
                maxRound = 5,
                gameDataDao = dataDao,
                wordRepository = wordRepository,
                context = this
            )
            else -> null // Handle invalid cases gracefully
        }
    }

    fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.gamescreen_fcv, fragment)
            .commit()
    }

    fun checkDictionaryWord(wordModel: WordModel) {
        val intent = Intent(this, WordDetailActivity::class.java)
        intent.putExtra("word_id", wordModel.id)
        intent.putExtra("word_text", wordModel.word) // Truyền từ cần hiển thị
        intent.putExtra("word_pronunciation", wordModel.pronunciation)
        intent.putExtra("word_details", wordModel.details)
        this.startActivity(intent)
    }


}