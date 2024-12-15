package com.android_ai.csc13009.app.presentation.Adapters

import android.app.Activity
import android.content.ClipData
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R

class GameAnswerBlocksAdapter(
    private val context: Activity,
    private val charaterList: List<Char>
) : RecyclerView.Adapter<GameAnswerBlocksAdapter.GameAnswerBlocksViewHolder>() {
    inner class GameAnswerBlocksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val answerBlockTextView: TextView = itemView.findViewById(R.id.item_letter_box)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameAnswerBlocksViewHolder {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.item_letter_dragable_box, parent, false)
        return GameAnswerBlocksViewHolder(rowView)
    }

    override fun getItemCount(): Int {
        return charaterList.size
    }

    override fun onBindViewHolder(holder: GameAnswerBlocksViewHolder, position: Int) {
        val char = charaterList[position]
        holder.answerBlockTextView.text = char.toString()

        // implement dragable
        val view = holder.itemView
        view.setOnTouchListener(fun(v: View, event: MotionEvent): Boolean {
            return if (event.action == MotionEvent.ACTION_DOWN) {
                val clipData = ClipData.newPlainText("letter", char.toString())
                val shadowBuilder = View.DragShadowBuilder(v)
                v.startDragAndDrop(clipData, shadowBuilder, v, 0)

                v.tag = position
                true
            } else {
                false
            }
        })

    }


}