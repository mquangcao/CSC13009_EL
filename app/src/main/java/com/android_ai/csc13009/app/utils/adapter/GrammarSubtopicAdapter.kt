package com.android_ai.csc13009.app.utils.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.domain.repository.model.GrammarSubtopic


class GrammarSubtopicAdapter(
    private val subtopics: List<GrammarSubtopic>
) : RecyclerView.Adapter<GrammarSubtopicAdapter.GrammarSubtopicViewHolder>() {

    // ViewHolder cho mỗi item trong danh sách
    class GrammarSubtopicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subtopicNameTextView: TextView = itemView.findViewById(R.id.textSubtopicName)
        val subtopicDetailsLayout: View = itemView.findViewById(R.id.subtopicDetailsLayout)
        val subtopicContentTextView: TextView = itemView.findViewById(R.id.subtopicContentTextView)
        val subtopicStructuresTextView: TextView = itemView.findViewById(R.id.subtopicStructuresTextView)
        val subtopicExamplesTextView: TextView = itemView.findViewById(R.id.subtopicExamplesTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GrammarSubtopicViewHolder {
        // Inflate layout cho mỗi item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_subtopic, parent, false)
        return GrammarSubtopicViewHolder(view)
    }

    override fun onBindViewHolder(holder: GrammarSubtopicViewHolder, position: Int) {
        // Gắn dữ liệu vào ViewHolder
        val subtopic = subtopics[position]
        holder.subtopicNameTextView.text = subtopic.name
        holder.subtopicContentTextView.text = subtopic.content
        holder.subtopicStructuresTextView.text = subtopic.structures
        holder.subtopicExamplesTextView.text = subtopic.examples

        // Xử lý sự kiện nhấn vào item
        holder.itemView.setOnClickListener {
            if (holder.subtopicDetailsLayout.visibility == View.GONE) {
                holder.subtopicDetailsLayout.visibility = View.VISIBLE
            } else {
                holder.subtopicDetailsLayout.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int = subtopics.size
}