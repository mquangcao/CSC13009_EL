package com.android_ai.csc13009.app.presentation.fragment.games

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.utils.adapters.GameAnswerBlocksAdapter
import com.android_ai.csc13009.app.utils.adapters.GameAnswerSlotsAdapter
import com.android_ai.csc13009.app.presentation.activity.GameActivity
import com.android_ai.csc13009.app.utils.extensions.NavigationSetter
import com.android_ai.csc13009.app.utils.extensions.TTSHelper
import com.android_ai.csc13009.app.utils.extensions.games.IGameEngine
import com.android_ai.csc13009.app.utils.extensions.games.LexiconGameEngine
import com.android_ai.csc13009.app.utils.extensions.games.SpellingBeeGameEngine
import com.android_ai.csc13009.app.utils.extensions.games.WordGameEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.ArrayList

class GameSessionFragment : Fragment(), GameInterface {
    private var gameEngine: IGameEngine? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_session, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity() as GameActivity
        gameEngine = activity.gameEngine!!


        setBackButton()
        setHighScoreText()

        activity.showLoading()

        CoroutineScope(Dispatchers.IO).launch {
            startGame()

            // quay lai main thread de tao ui
            withContext(Dispatchers.Main) {
                load()
            }
        }
    }



    private fun setBackButton() {
        val toolbar: Toolbar = requireView().findViewById(R.id.game_result_header_tb)

        val backTitle = "Are you sure?"
        val backMessage = "Do you really want to end the game and go back?"
        val activity = requireActivity() as AppCompatActivity

        NavigationSetter.setBackButton(toolbar, activity)
    }



    private suspend fun startGame() {
        gameEngine?.startGame()
    }

    private fun setScoreText() {
        val scoreTextView: TextView = requireView().findViewById(R.id.game_session_score_value_tv)
        scoreTextView.text = "${gameEngine?.score}"
    }

    private fun setHighScoreText() {
        val scoreTextView = requireView().findViewById<TextView>(R.id.game_session_header_tb_title)
        scoreTextView.text = "High Score: ${gameEngine?.highScore}"
    }

    private fun setCanvas(){
        when (gameEngine?.javaClass) {
            LexiconGameEngine::class.java -> {
                setQuestion(R.layout.custom_view_game_question_text)
                setAnswerListWriter(R.layout.custom_view_game_answer_list_writer)
            }
            SpellingBeeGameEngine::class.java -> {
                setQuestion(R.layout.custom_view_game_question_audio)
                setAnswerLetterPicker(R.layout.custom_view_game_answer_letter_picker)
            }
            WordGameEngine::class.java -> {
                setQuestion(R.layout.custom_view_game_question_image)
                setAnswerLetterPicker(R.layout.custom_view_game_answer_letter_picker)
            }
            else -> {
                // do nothing
            }
        }
    }

    private fun setQuestion(layout: Int) {
        val canvas = requireView().findViewById<LinearLayout>(R.id.game_canvas_ll)
        val inflater = LayoutInflater.from(requireContext())
        val questionView = inflater.inflate(layout, canvas, false)
        val questionLayout = canvas.findViewById<FrameLayout>(R.id.game_canvas_question_fl)
        questionLayout.removeAllViews()
        questionLayout.addView(questionView)

        when (gameEngine?.javaClass) {
            LexiconGameEngine::class.java -> {
                setQuestionLexiconGame(questionView)
            }
            SpellingBeeGameEngine::class.java -> {
                setQuestionSpellingBeeGame(questionView)
            }
            WordGameEngine::class.java -> {
                setQuestionWordGame(questionView)
            }
        }
    }

    private fun setQuestionSpellingBeeGame(view: View) {
        val word = gameEngine?.currentWord?.word
        val pronunciation = gameEngine?.currentWord?.pronunciation

        val audioButton = view.findViewById<ImageButton>(R.id.game_session_question_content)
        val audioProgress = view.findViewById<ProgressBar>(R.id.game_session_question_content_extra)
        val wordTextView = view.findViewById<TextView>(R.id.game_session_question_prompt)

        val stringBuilder = StringBuilder()
        stringBuilder.appendLine("Spell this word:")
        stringBuilder.appendLine(pronunciation)
        wordTextView.text = stringBuilder.toString()
        val handler = Handler(Looper.getMainLooper())
        var updateProgress: Runnable? = null

        val tts = TTSHelper(requireContext())
        audioButton.setOnClickListener {
            if (word != null) {
                tts.stop()
                updateProgress?.let { it -> handler.removeCallbacks(it) }

                audioProgress.progress = 0
                val estimatedDuration = tts.estimateSpeechDuration(word)
                val updateInterval = 100 // Update every 100ms


                tts.speak(word)


                val startTime = System.currentTimeMillis()

                updateProgress = object : Runnable {
                    override fun run() {
                        val elapsedTime = System.currentTimeMillis() - startTime
                        val progress = ((elapsedTime.toFloat() / estimatedDuration) * 100).toInt()
                        audioProgress.progress = progress

                        if (elapsedTime < estimatedDuration) {
                            handler.postDelayed(this, updateInterval.toLong())
                        } else {
                            audioProgress.progress = 100
                        }
                    }
                }

                handler.post(updateProgress as Runnable)
            }
        }
    }

    private fun setQuestionWordGame(view: View) {
        val wordTextView = view.findViewById<TextView>(R.id.game_session_question_prompt)
        val word = gameEngine?.currentWord?.word
        val scrambledWord = scrambleWord(word?: "")
        wordTextView.text = "Unscramble this word \n $scrambledWord"
    }

    private fun scrambleWord(word: String, minGroupSize: Int = 2, maxGroupSize: Int = 4): String {
        if (word.length <= minGroupSize) return word.toCharArray().toList().shuffled().joinToString("/")

        val chunks = mutableListOf<String>()
        var i = 0

        while (i < word.length) {
            val groupSize = (minGroupSize..maxGroupSize).random() // Randomly choose group size
            val end = (i + groupSize).coerceAtMost(word.length) // Ensure within bounds
            chunks.add(word.substring(i, end))
            i = end
        }

        return chunks.shuffled().joinToString("/")
    }

    private fun setQuestionLexiconGame(view: View) {
        val wordTextView = view.findViewById<TextView>(R.id.game_session_question_prompt)

        val lexiconGameEngine = gameEngine as LexiconGameEngine;
        val conditionText = lexiconGameEngine.currentCondition?.getConditionPrompt();

        val stringbuilder = StringBuilder();
        stringbuilder.appendLine(conditionText);

        stringbuilder.appendLine("Entered words:")

        val roundCount = lexiconGameEngine.maxRound;
        val words = lexiconGameEngine.words
        for (i in 1..roundCount) {
            var text = "${i}.)";
            if (i <= words.size) {
                text += words[i-1].word
            }
            stringbuilder.appendLine(text);
        }

        wordTextView.text = stringbuilder.toString();

    }

    private fun setAnswer(layout: Int) {
        val canvas = requireView().findViewById<LinearLayout>(R.id.game_canvas_ll)
        val inflater = LayoutInflater.from(requireContext())
        val answerView = inflater.inflate(layout, canvas, false)
        val answerLayout = canvas.findViewById<FrameLayout>(R.id.game_canvas_answer_fl)
        answerLayout.removeAllViews()
        answerLayout.addView(answerView)
    }

    private fun setAnswerLetterPicker(layout: Int) {
        setAnswer(layout)
        // set adapter for answer choices (n+ items for n-length word (n right and 1+ wrong))
        val correctAnswer = gameEngine!!.currentWord!!.word
        val extraCount = correctAnswer.length / 2

        val randomExtraChars = (1..extraCount)
            .map { ('A'..'Z').random() }
            .joinToString("")

        val answerWithExtraChars = "${correctAnswer}${randomExtraChars}"

        val scrambledWord = answerWithExtraChars.toCharArray().toList().shuffled()

        val answerBlocksAdapter = GameAnswerBlocksAdapter(requireActivity(), ArrayList(scrambledWord))
        val answerBlocks = requireView().findViewById<RecyclerView>(R.id.game_answer_letter_picker_blocks)
        answerBlocks.adapter = answerBlocksAdapter
        answerBlocks.layoutManager = GridLayoutManager(requireContext(), 6)

        // set adapter for answer slots (n items for n-length word)
        val inputList = MutableList<Char?> (correctAnswer.length) { null }
        val answerSlotsAdapter = GameAnswerSlotsAdapter(requireActivity(), inputList, answerBlocksAdapter)
        val answerSlots = requireView().findViewById<RecyclerView>(R.id.game_answer_letter_picker_slots)
        answerSlots.adapter = answerSlotsAdapter
        answerSlots.layoutManager = GridLayoutManager(requireContext(), 6)
    }

    private fun setAnswerListWriter(layout: Int) {
        setAnswer(layout)
        // set event listeners
        val answerList = requireView().findViewById<RecyclerView>(R.id.game_answer_writer_list)
        val answerEd = requireView().findViewById<TextView>(R.id.game_answer_writer_ed)

        // when enter button is pressed
        answerEd.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event?.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)) {

                val enteredText = answerEd.text.toString().trim()

                // Add the entered text to the RecyclerView's adapter
                if (enteredText.isNotEmpty()) {
                    val activity = requireActivity() as GameActivity
                    activity.submitAnswer(enteredText);
                }
                true // Indicate the event was handled
            } else {
                false // Let the system handle other keys
            }

        }
    }

    private fun setAnswerListReader(layout: Int) {

    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun toResultFragment() {
        (requireActivity() as GameActivity).changeFragment(GameResultFragment.newInstance())
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            GameSessionFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun load() {
        setCanvas()
        setScoreText()

        (activity as GameActivity).hideLoading()
    }

    @SuppressLint("NewApi")
    override fun nextRound() {
        if (gameEngine!!.gameState == IGameEngine.GameState.FINISHED) {
            toResultFragment()
            return
        }
        load()
    }
}