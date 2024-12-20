package com.android_ai.csc13009.app.utils.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.domain.models.GrammarTopic
import com.android_ai.csc13009.app.data.local.entity.GrammarTopicEntity

class GrammarTopicAdapter(
    private val topics: List<GrammarTopic>,
    private val onItemClick: (GrammarTopicEntity) -> Unit
) : RecyclerView.Adapter<GrammarTopicAdapter.GrammarTopicViewHolder>() {

    // ViewHolder cho mỗi item trong danh sách
    class GrammarTopicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val topicNameTextView: TextView = itemView.findViewById(R.id.textTopicName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GrammarTopicViewHolder {
        // Inflate layout cho mỗi item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_grammar_topic, parent, false)
        return GrammarTopicViewHolder(view)
    }

    override fun onBindViewHolder(holder: GrammarTopicViewHolder, position: Int) {
        // Gắn dữ liệu vào ViewHolder
        val topic = topics[position]
        holder.topicNameTextView.text = topic.name

        // Xử lý sự kiện nhấn vào item
        holder.itemView.setOnClickListener {
            // Chuyển đổi từ GrammarTopic sang GrammarTopicEntity
            val topicEntity = GrammarTopicEntity(
                id = topic.id,
                name = topic.name,
                levelId = topic.levelId
            )
            onItemClick(topicEntity)
        }
    }

    override fun getItemCount(): Int = topics.size
}
