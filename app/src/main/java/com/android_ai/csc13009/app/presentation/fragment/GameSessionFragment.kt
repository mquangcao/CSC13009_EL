package com.android_ai.csc13009.app.presentation.fragment

import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.utils.adapters.GameAnswerBlocksAdapter
import com.android_ai.csc13009.app.utils.adapters.GameAnswerSlotsAdapter
import com.android_ai.csc13009.app.presentation.activity.GameActivity
import com.android_ai.csc13009.app.utils.extensions.games.IGameEngine
import com.android_ai.csc13009.app.utils.extensions.games.SpellingBeeGameEngine
import com.android_ai.csc13009.app.utils.extensions.games.SynonymGameEngine
import com.android_ai.csc13009.app.utils.extensions.games.WordGameEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.ArrayList

//private const val ARG_ENGN_IDX = "GameEngineIndex"

class GameSessionFragment : Fragment() {
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

        setScoreText()
        setHighScoreText()

        CoroutineScope(Dispatchers.IO).launch {
            startGame()

            // quay lai main thread de tao ui
            withContext(Dispatchers.Main) {
                setCanvas()
            }
        }
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
            SpellingBeeGameEngine::class.java -> {
                setQuestion(R.layout.custom_view_game_question_audio)
                setAnswerLetterPicker(R.layout.custom_view_game_answer_letter_picker)
            }
            SynonymGameEngine::class.java -> {
                setQuestion(R.layout.custom_view_game_question_text)
                setAnswerListWriter(R.layout.custom_view_game_answer_list_writer)
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
            SpellingBeeGameEngine::class.java -> {
//                setQuestionSpellingBeeGame(questionView)
            }
            SynonymGameEngine::class.java -> {
//                setQuestionSynonymGame(questionView)
            }
            WordGameEngine::class.java -> {
                setQuestionWordGame(questionView)
            }
        }
    }

    private fun setQuestionWordGame(view: View) {
        val wordTextView = view.findViewById<TextView>(R.id.game_session_question_prompt)
        wordTextView.text = "What is this?"
        // debug
        wordTextView.text = "What is this \n ${gameEngine?.currentWord?.word}"

//        val wordImageView = view.findViewById<ImageView>(R.id.game_session_question_content)
//        val uri = gameEngine?.currentWord?.image
//        val bitmap = bit
//        wordImageView.setImageBitmap(gameEngine?.currentWord?.image)
    }

    private fun setQuestionSpellingBeeGame(view: View) {
        val wordTextView = view.findViewById<TextView>(R.id.game_session_question_prompt)
        wordTextView.text = "Spell this word"
        // debug
        wordTextView.text = "Spell this word (${gameEngine?.currentWord?.word})"

//        val audioPlayer = view.findViewById<ImageButton>(R.id.game_session_question_content)
//        audioPlayer.setOnClickListener(view, event -> {
//            val audio = gameEngine?.currentWord?.audio
//        })
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
        val correctAnswer = gameEngine?.words?.lastOrNull()?.word ?: "default"
        val extraCount = correctAnswer.length / 2

        val randomExtraChars = (1..extraCount)
            .map { ('A'..'Z').random() }
            .joinToString("")

        val answerWithExtraChars = "$correctAnswer$randomExtraChars"

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
                    gameEngine?.submitAnswer(enteredText)
                }

                true // Indicate the event was handled
            } else {
                false // Let the system handle other keys
            }
        }
    }

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
}