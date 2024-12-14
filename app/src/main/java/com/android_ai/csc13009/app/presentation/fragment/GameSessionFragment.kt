package com.android_ai.csc13009.app.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.presentation.activity.GameActivity
import com.android_ai.csc13009.app.presentation.service.IGameEngine

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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GameSessionFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(gameEngineIndex: Int) =
            GameSessionFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_ENGN_IDX, gameEngineIndex)
                }
            }
    }
}