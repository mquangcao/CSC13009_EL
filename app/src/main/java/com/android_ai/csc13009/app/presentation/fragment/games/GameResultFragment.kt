package com.android_ai.csc13009.app.presentation.fragment.games

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.domain.models.WordModel
import com.android_ai.csc13009.app.presentation.activity.GameActivity
import com.android_ai.csc13009.app.utils.adapter.DictionaryAdapter
import com.android_ai.csc13009.app.utils.extensions.games.IGameEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GameResultFragment : Fragment() {
    private lateinit var gameEngine: IGameEngine

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
        return inflater.inflate(R.layout.fragment_game_result, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gameEngine = (requireActivity() as GameActivity).gameEngine!!
        val scoreView = view.findViewById<TextView>(R.id.game_result_score_content_tv)
        val score = gameEngine.score
        var extraText = ""
        if (score > gameEngine.highScore) {
            extraText = "\nNew Record!"
        }
        val text = "${score}$extraText"
        scoreView.text = text

        CoroutineScope(Dispatchers.IO).launch {
            gameEngine.updateHighScore()
        }

        val funt : (WordModel) -> Unit = {

        }

        val featuredWordView = view.findViewById<RecyclerView>(R.id.game_result_word_list_rv)
        val dictionaryAdapter = DictionaryAdapter(gameEngine.words, funt)
        featuredWordView.adapter = dictionaryAdapter
        featuredWordView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())

        val returnButton = view.findViewById<Button>(R.id.game_exit_bt)
        returnButton.setOnClickListener {
            requireActivity().finish()
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            GameResultFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}