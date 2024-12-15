package com.android_ai.csc13009.app.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R

class StatisticsVocabularyDetailAdapter(private val vocabularyList: List<String>) :
    RecyclerView.Adapter<StatisticsVocabularyDetailAdapter.VocabularyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VocabularyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.statistics_item_vocabulary, parent, false)  // Sử dụng layout tùy chỉnh
        return VocabularyViewHolder(view)
    }

    override fun onBindViewHolder(holder: VocabularyViewHolder, position: Int) {
        holder.bind(vocabularyList[position])
    }

    override fun getItemCount(): Int {
        return vocabularyList.size
    }

    inner class VocabularyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val wordTextView: TextView = itemView.findViewById(R.id.textVocabularyWord)

        fun bind(word: String) {
            wordTextView.text = word
        }
    }
}
