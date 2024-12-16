package com.android_ai.csc13009.app.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.presentation.Adapters.GameSelectorAdapter
import com.android_ai.csc13009.app.presentation.activity.GameActivity
import com.android_ai.csc13009.app.presentation.service.IGameEngine
import com.android_ai.csc13009.app.presentation.service.SpellingBeeGameEngine
import com.android_ai.csc13009.app.presentation.service.SynonymGameEngine
import com.android_ai.csc13009.app.presentation.service.WordGameEngine


class GameSelectorFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = requireView().findViewById(R.id.game_selector_list_rv)
        val activity = requireActivity() as GameActivity
        val adapter = GameSelectorAdapter(activity, activity.gameEngines)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())
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
            GameSelectorFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}