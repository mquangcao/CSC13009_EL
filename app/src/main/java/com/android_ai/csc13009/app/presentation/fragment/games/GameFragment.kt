package com.android_ai.csc13009.app.presentation.fragment.games

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.data.local.AppDatabase
import com.android_ai.csc13009.app.data.local.repository.WordRepository
import com.android_ai.csc13009.app.utils.adapters.GameSelectorAdapter
import com.android_ai.csc13009.app.utils.extensions.games.IGameEngine
import com.android_ai.csc13009.app.utils.extensions.games.LexiconGameEngine
import com.android_ai.csc13009.app.utils.extensions.games.SynonymGameEngine
import com.android_ai.csc13009.app.utils.extensions.games.WordGameEngine


class GameFragment : Fragment() {
    private var configMaxRound: Int = 5;
    private var configSessionDuration: Int = 60;
    private val database : AppDatabase by lazy { AppDatabase.getInstance(requireContext()) }

    public val gameEngines: List<IGameEngine> by lazy {
        createGameEngines()
    }

    private fun createGameEngines(): List<IGameEngine> {
        val wordDao = database.wordDao();
        val wordRepository = WordRepository(wordDao)
        return listOf(
            LexiconGameEngine(
                maxRound = configMaxRound,
                gameDataDao = database.gameDataDao(),
                wordRepository = wordRepository
            )
            , SynonymGameEngine(
                maxRound = configMaxRound,
                gameDataDao = database.gameDataDao(),
                wordRepository = wordRepository,
                currentRound = 1
            )
            ,  WordGameEngine(
                maxRound = configMaxRound,
                gameDataDao = database.gameDataDao(),
                wordRepository = wordRepository
            )
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = requireView().findViewById(R.id.game_selector_list_rv)
        val adapter = GameSelectorAdapter(requireActivity(), gameEngines)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_selector, container, false)
    }

    companion object {
        fun newInstance(param1: Int, param2: Int) =
            GameFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}