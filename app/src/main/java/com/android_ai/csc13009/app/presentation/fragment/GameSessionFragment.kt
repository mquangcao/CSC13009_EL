package com.android_ai.csc13009.app.presentation.fragment

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.presentation.Adapters.GameAnswerBlocksAdapter
import com.android_ai.csc13009.app.presentation.Adapters.GameAnswerSlotsAdapter
import com.android_ai.csc13009.app.presentation.activity.GameActivity
import com.android_ai.csc13009.app.presentation.service.IGameEngine
import com.android_ai.csc13009.app.presentation.service.SpellingBeeGameEngine
import com.android_ai.csc13009.app.presentation.service.SynonymGameEngine
import com.android_ai.csc13009.app.presentation.service.WordGameEngine
import kotlin.random.Random

private const val ARG_ENGN_IDX = "GameEngineIndex"

class GameSessionFragment : Fragment() {
    private var gameEngine: IGameEngine? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val index = it.getInt(ARG_ENGN_IDX)
            val activity = requireActivity() as GameActivity
            gameEngine = activity.gameEngines[index]
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_session, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setScoreText()
        setHighScoreText()
        setCanvas()
        startGame()
    }

    private fun startGame() {
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
        val canvas = requireView().findViewById<FrameLayout>(R.id.game_canvas_fl)
        val inflater = LayoutInflater.from(requireContext())
        val questionView = inflater.inflate(layout, canvas, false)
        val questionLayout = canvas.findViewById<FrameLayout>(R.id.game_canvas_question_fl)
        questionLayout.removeAllViews()
        questionLayout.addView(questionView)
    }

    private fun setAnswer(layout: Int) {
        val canvas = requireView().findViewById<FrameLayout>(R.id.game_canvas_fl)
        val inflater = LayoutInflater.from(requireContext())
        val answerView = inflater.inflate(layout, canvas, false)
        val answerLayout = canvas.findViewById<FrameLayout>(R.id.game_canvas_answer_fl)
        answerLayout.removeAllViews()
        answerLayout.addView(answerView)
    }

    private fun setAnswerLetterPicker(layout: Int) {
        setAnswer(layout)
        // set adapter for answer choices (n+ items for n-length word (n right and 1+ wrong))
        val correctAnswer = gameEngine?.words?.get(gameEngine?.words?.size?.minus(1) ?: 0)
        val rnd = Random(System.currentTimeMillis())
        val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"

        val extraCount = (correctAnswer?.word?.length ?: 0) / 2

        val randomExtraString = StringBuilder("")
        for (i in 1..extraCount) {
            randomExtraString.append(alphabet.get(rnd.nextInt() % alphabet.length))
        }
        val answerWithExtraChar = "${correctAnswer?.word}${randomExtraString}"
        val scrambledWord = answerWithExtraChar.toCharArray().toList().shuffled()

        val answerBlocksAdapter = GameAnswerBlocksAdapter(requireActivity(), scrambledWord)
        val answerBlocks = requireView().findViewById<RecyclerView>(R.id.game_answer_letter_picker_blocks)
        answerBlocks.adapter = answerBlocksAdapter
        answerBlocks.layoutManager = androidx.recyclerview.widget.GridLayoutManager(requireContext(), 8)

        // set adapter for answer slots (n items for n-length word)
        val answerSlotsAdapter = GameAnswerSlotsAdapter(requireActivity(), correctAnswer?.word ?: "", answerBlocksAdapter, scrambledWord)
        val answerSlots = requireView().findViewById<RecyclerView>(R.id.game_answer_letter_picker_slots)
        answerSlots.adapter = answerSlotsAdapter
        answerSlots.layoutManager = androidx.recyclerview.widget.GridLayoutManager(requireContext(), correctAnswer?.word?.length ?: 0)
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

    companion object {
        @JvmStatic
        fun newInstance(gameEngineIndex: Int) =
            GameSessionFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_ENGN_IDX, gameEngineIndex)
                }
            }
    }
}