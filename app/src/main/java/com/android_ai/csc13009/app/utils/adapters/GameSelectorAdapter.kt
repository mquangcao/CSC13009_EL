package com.android_ai.csc13009.app.utils.adapters

import android.app.Activity
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.presentation.activity.DashboardActivity
import com.android_ai.csc13009.app.presentation.activity.GameActivity


import com.android_ai.csc13009.app.utils.extensions.games.IGameEngine
import com.android_ai.csc13009.app.utils.extensions.games.LexiconGameEngine
import com.android_ai.csc13009.app.utils.extensions.games.SpellingBeeGameEngine
import com.android_ai.csc13009.app.utils.extensions.games.WordGameEngine
import java.io.File

class GameSelectorAdapter (
    private val context: Activity,
    private val gameEngines: List<IGameEngine>
) : RecyclerView.Adapter<GameSelectorAdapter.GameSelectorViewHolder>() {

    inner class GameSelectorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.game_item_title_tv)
        val scoreTextView: TextView = itemView.findViewById(R.id.game_item_score_tv)
        val background: ImageView = itemView.findViewById(R.id.item_game_selection_bg)
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
        when(gameEngine.javaClass) {
            LexiconGameEngine::class.java -> {
                holder.background.setImageBitmap(BitmapFactory.decodeResource(context.resources, R.drawable.game_logo_lexicon))
            }
            SpellingBeeGameEngine::class.java -> {
                holder.background.setImageBitmap(BitmapFactory.decodeResource(context.resources, R.drawable.game_logo_bee))
            }
            WordGameEngine::class.java -> {
                holder.background.setImageBitmap(BitmapFactory.decodeResource(context.resources, R.drawable.game_logo_unscramble))
            }
            else -> {
                // do nothing
            }
        }

        holder.titleTextView.text = gameEngine.getGameName()

        val highScoreString = StringBuilder(context.getString(R.string.game_high_score))
        highScoreString.append(" ${gameEngine.highScore}")
        holder.scoreTextView.text = highScoreString.toString()

        holder.itemView.setOnClickListener {
            val activityContext = context as DashboardActivity

            activityContext.changeActivity(
                context = activityContext,
                targetActivity = GameActivity::class.java,
                passedData = position
            )
        }
    }
}