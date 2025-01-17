package com.android_ai.csc13009.app.utils.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.domain.models.StoryItem
import com.android_ai.csc13009.app.utils.ChatBubbleView

class StoryAdapter(private val items: MutableList<StoryItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_NARRATION = 0
        private const val TYPE_MESSAGE = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is StoryItem.Narration -> TYPE_NARRATION
            is StoryItem.Message -> TYPE_MESSAGE
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_NARRATION -> {
                val view = inflater.inflate(R.layout.item_narration, parent, false)
                NarrationViewHolder(view)
            }
            TYPE_MESSAGE -> {
                val view = inflater.inflate(R.layout.item_message, parent, false)
                MessageViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NarrationViewHolder -> {
                val item = items[position] as StoryItem.Narration
                holder.textNarration.text = item.text
            }
            is MessageViewHolder -> {
                val item = items[position] as StoryItem.Message
                holder.textMessage.setText(item.text)
            }
        }
    }


    override fun getItemCount(): Int = items.size

    class NarrationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textNarration: TextView = itemView.findViewById(R.id.textNarration)
    }

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textMessage: ChatBubbleView = itemView.findViewById(R.id.textMessage)
    }
}