//package com.android_ai.csc13009.app.utils.adapters
//
//import android.app.Activity
//import android.view.DragEvent
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.android_ai.csc13009.R
//import com.android_ai.csc13009.app.presentation.activity.GameActivity
//
//class GameAnswerSlotsAdapter (
//    private val context: Activity,
//    private val word: String,
//    private val sourceAdapter: GameAnswerBlocksAdapter,
//    private val charaterList: List<Char>
//) : RecyclerView.Adapter<GameAnswerSlotsAdapter.GameAnswerSlotsViewHolder>() {
//
//    inner class GameAnswerSlotsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val answerSlotTextView: TextView = itemView.findViewById(R.id.item_letter_slot)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameAnswerSlotsViewHolder {
//        val inflater = context.layoutInflater
//        val rowView = inflater.inflate(R.layout.item_letter_slot, parent, false)
//        return GameAnswerSlotsViewHolder(rowView)
//    }
//
//    override fun getItemCount(): Int {
//        return word.length
//    }
//
//    override fun onBindViewHolder(holder: GameAnswerSlotsViewHolder, position: Int) {
////        val char = word[position]
//        holder.answerSlotTextView.text = ""
//
//        val view = holder.itemView
//
//        view.setOnDragListener { v, event ->
//            when (event.action) {
//                DragEvent.ACTION_DRAG_STARTED -> {
//                    // Highlight the border of the slot to indicate it's ready to accept
//                    v.setBackgroundResource(R.drawable.rounded_rectangle_highlight_border)
//                    true
//                }
//                DragEvent.ACTION_DRAG_ENTERED -> {
//                    // Further emphasize the slot when an item is hovering
////                    v.
//                    true
//                }
//                DragEvent.ACTION_DROP -> {
//                    // Retrieve data from the dragged item
//                    if (event.clipDescription?.label == "letter") {
//                        val draggedData = event.clipData.getItemAt(0).text.toString()
//                        if ((v as TextView).text.isEmpty()) {
//                            v.text = draggedData
//
//                            val sourceView = event.localState as View
//                            val position = sourceView.tag as Int
//
//                            val activityContext = context as GameActivity
//                            activityContext.runOnUiThread {
//                                // notify adapter to remove item from list
////                                activityContext.gameAnswerBlocksAdapter.(position)
//                                charaterList.toMutableList().removeAt(position)
//                                sourceAdapter.notifyItemRemoved(position)
//                            }
//                        }
//
//                        (v as TextView).text = draggedData
//                    }
//
//                    v.setBackgroundResource(R.drawable.rounded_rectangle) // Reset the border
//                    true
//                }
//                DragEvent.ACTION_DRAG_EXITED -> {
//                    // Unhighlight the item
//                    v.setBackgroundResource(R.drawable.rounded_rectangle)
//                    true
//                }
//                DragEvent.ACTION_DRAG_ENDED -> {
//                    // Reset to default
//                    v.setBackgroundResource(R.drawable.rounded_rectangle)
//                    true
//                }
//                else -> false
//            }
//        }
//
//        // implement return
//        view.setOnClickListener {
//            // Check if the slot has any content
//            val slotContent = holder.answerSlotTextView.text.toString()
//            if (slotContent.isNotEmpty()) {
//                // Clear the slot
//                holder.answerSlotTextView.text = ""
//
//                // Notify the source adapter to add the item back
//                val parentActivity = context as GameActivity
//
//                //return item to source
//                charaterList.toMutableList().add(slotContent[0])
//                sourceAdapter.notifyItemInserted(charaterList.lastIndexOf(slotContent[0]))
//            }
//        }
//
//    }
//}