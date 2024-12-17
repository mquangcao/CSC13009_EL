package com.android_ai.csc13009.app.utils.adapters

import android.app.Activity
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.presentation.activity.GameActivity

class GameAnswerSlotsAdapter (
    private val context: Activity,
//    private val word: String,
    private val inputArray: MutableList<Char?>,
    private val sourceAdapter: GameAnswerBlocksAdapter,
//    private val charaterList: List<Char>
) : RecyclerView.Adapter<GameAnswerSlotsAdapter.GameAnswerSlotsViewHolder>() {

    inner class GameAnswerSlotsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val answerSlotTextView: TextView = itemView.findViewById(R.id.item_letter_slot_tv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameAnswerSlotsViewHolder {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.item_letter_slot, parent, false)
        return GameAnswerSlotsViewHolder(rowView)
    }

    override fun getItemCount(): Int {
        return inputArray.size
    }

    override fun onBindViewHolder(holder: GameAnswerSlotsViewHolder, position: Int) {
        val char = inputArray[position]
        if (char == null) {
            holder.answerSlotTextView.text = " "
        } else {
            holder.answerSlotTextView.text = char.toString().uppercase()
        }

        val view = holder.itemView

        view.setOnDragListener { _, event ->
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    if (char == null) {
                        view.setBackgroundResource(R.drawable.rounded_rectangle_highlight_border)
                    }
                    true
                }
                DragEvent.ACTION_DRAG_ENTERED -> {
                    // Further emphasize the slot when an item is hovering
                    if (char == null) {
                        view.setBackgroundResource(R.drawable.rounded_rectangle_hovered_border)
                    }
                    true
                }
                DragEvent.ACTION_DROP -> {
                    // Retrieve data from the dragged item
                    if (event.clipDescription?.label == "letter") {
                        val draggedData = event.clipData.getItemAt(0).text
                        if (char == null) {
                            val sourceView = event.localState as View
                            val sourcePosition = sourceView.tag as Int

                            val activityContext = context as GameActivity
                            activityContext.runOnUiThread {
                                inputArray[position] = draggedData[0]
                                notifyItemChanged(position)
                                sourceAdapter.deleteItem(sourcePosition)

                            }
                        }
                    }

                    view.setBackgroundResource(R.drawable.rounded_rectangle) // Reset the border
                    true
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                    if (char == null) {
                        view.setBackgroundResource(R.drawable.rounded_rectangle_highlight_border)
                    }
                    true
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    // Reset to default
                    view.setBackgroundResource(R.drawable.rounded_rectangle)
                    true
                }
                else -> false
            }
        }

        // implement return
        view.setOnClickListener {
            if (char != null) {
                val parentActivity = context as GameActivity
                parentActivity.runOnUiThread {
                    inputArray[position] = null
                    notifyItemChanged(position)
                    sourceAdapter.addItem(char)
                }
            }
        }

    }
}