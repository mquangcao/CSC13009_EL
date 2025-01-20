package com.android_ai.csc13009.app.utils.adapters

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
    public var charaterList: ArrayList<Char>,
    var onItemClicked: (char: Char ,position: Int) -> Unit
) : RecyclerView.Adapter<GameAnswerBlocksAdapter.GameAnswerBlocksViewHolder>() {
    inner class GameAnswerBlocksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val answerBlockTextView: TextView = itemView.findViewById(R.id.item_letter_box_tv)
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
        holder.answerBlockTextView.text = char.toString().uppercase()

        // implement draggable
        val view = holder.itemView
        view.setOnLongClickListener {
            val clipData = ClipData.newPlainText("letter", char.toString())
            val shadowBuilder = View.DragShadowBuilder(it)
            it.startDragAndDrop(clipData, shadowBuilder, it, 0)
            it.tag = holder.adapterPosition

            true
        }


        view.setOnClickListener {
            onItemClicked(char, holder.adapterPosition)
        }

//
//        view.setOnTouchListener(fun(v: View, event: MotionEvent): Boolean {
//            return if (event.action == MotionEvent.ACTION_DOWN) {
//                val clipData = ClipData.newPlainText("letter", char.toString())
//                val shadowBuilder = View.DragShadowBuilder(v)
//                v.startDragAndDrop(clipData, shadowBuilder, v, 0)
//                val currentPosition = holder.adapterPosition
//                v.tag = currentPosition
//
//                true
//            } else {
//                false
//            }
//        })

    }

    public fun deleteItem(position: Int) {
        charaterList.removeAt(position)
        notifyItemRemoved(position)
//        notifyItemRangeChanged(position, charaterList.size)
    }

    public fun addItem(char: Char) {
        charaterList.add(char)
        notifyItemInserted(charaterList.size - 1)
    }


}