package com.android_ai.csc13009.app.utils.adapters

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R

class GameAnswerListAdapter (
    private val context: Activity,
    private val AnswerList: List<String>
) : RecyclerView.Adapter<GameAnswerListAdapter.GameAnswerListViewHolder>() {

    class GameAnswerListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val answerTextView: TextView = itemView.findViewById(R.id.item_letter_box)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameAnswerListViewHolder {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.item_letter_slot, parent, false)
        return GameAnswerListViewHolder(rowView)
    }

    override fun getItemCount(): Int {
        return AnswerList.size
    }

    override fun onBindViewHolder(holder: GameAnswerListViewHolder, position: Int) {
        val answer = AnswerList[position]
        holder.answerTextView.text = answer
    }


}