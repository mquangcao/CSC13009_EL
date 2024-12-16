package com.android_ai.csc13009.app.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.presentation.activity.GameActivity
import com.android_ai.csc13009.app.utils.extensions.games.IGameEngine

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_ENGN_IDX = "GameEngineIndex"

/**
 * A simple [Fragment] subclass.
 * Use the [GameResultFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameResultFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var gameEngineIndex: Int? = null
    private var gameEngine: IGameEngine? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            gameEngineIndex = it.getInt(ARG_ENGN_IDX)
            gameEngine = (requireActivity() as GameActivity).gameEngines[gameEngineIndex!!]
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_result, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GameResultFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(gameEngineIndex: Int) =
            GameResultFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_ENGN_IDX, gameEngineIndex)
                }
            }
    }
}