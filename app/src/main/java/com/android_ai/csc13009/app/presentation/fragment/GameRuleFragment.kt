package com.android_ai.csc13009.app.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.presentation.activity.GameActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_GAME_ENGINE_IDX = "GameEngineIndex"

/**
 * A simple [Fragment] subclass.
 * Use the [GameRuleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameRuleFragment : Fragment() {
    private var gameEngineIndex: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            gameEngineIndex = it.getInt(ARG_GAME_ENGINE_IDX)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_rule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity() as GameActivity
        val gameEngine = activity.gameEngines[gameEngineIndex!!]

        setRuleText(gameEngine.getRule())
        setStartButtonClickListener {
            val gameSessionFragment = GameSessionFragment.newInstance(gameEngineIndex!!)
            activity.changeFragment(gameSessionFragment)
        }

        val ruleTextView: TextView = view.findViewById(R.id.game_rule_header_tb_title)
        ruleTextView.text = gameEngine.gameName

        val gameNameTextView = view.findViewById<TextView>(R.id.game_rule_content_tv)
        gameNameTextView.text = gameEngine.getRule()

        val highScoreTextView = view.findViewById<TextView>(R.id.game_high_score_value_tv)
        highScoreTextView.text = "${gameEngine.highScore}"

    }

    private fun setRuleText(rule: String) {
        val ruleTextView: TextView = requireView().findViewById(R.id.game_rule_content_tv)
        ruleTextView.text = rule
    }

    private fun setStartButtonClickListener(listener: View.OnClickListener) {
        val button: Button = requireView().findViewById(R.id.game_start_bt)
        button.setOnClickListener(listener)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int) =
            GameRuleFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_GAME_ENGINE_IDX, param1)
                }
            }
    }
}