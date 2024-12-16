package com.android_ai.csc13009.app.utils.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.presentation.activity.GameActivity
import com.android_ai.csc13009.app.presentation.fragment.GameRuleFragment


import com.android_ai.csc13009.app.utils.extensions.games.IGameEngine

class GameSelectorAdapter (
    private val context: Activity,
    private val gameEngines: List<IGameEngine>
) : RecyclerView.Adapter<GameSelectorAdapter.GameSelectorViewHolder>() {

    inner class GameSelectorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.game_item_title_tv)
        val scoreTextView: TextView = itemView.findViewById(R.id.game_item_score_tv)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameSelectorViewHolder {
        val inflater = LayoutInflater.from(context)
        val rowView = inflater.inflate(R.layout.item_game_selection, parent, false)
        return GameSelectorViewHolder(rowView)
    }

    override fun getItemCount(): Int {
        return gameEngines.size
    }

    override fun onBindViewHolder(holder: GameSelectorViewHolder, position: Int) {
        val gameEngine = gameEngines[position]

        holder.titleTextView.text = gameEngine.getGameName()

        val highScoreString = StringBuilder(context.getString(R.string.game_high_score))
        highScoreString.append(" ${gameEngine.highScore}")
        holder.scoreTextView.text = highScoreString.toString()

        holder.itemView.setOnClickListener {
            val gameRuleFragment = GameRuleFragment.newInstance(position)
            val activityContext = context as GameActivity
            activityContext.changeFragment(gameRuleFragment)
        }
    }
}