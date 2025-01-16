package com.android_ai.csc13009.app.utils.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.domain.models.WordModel
import com.android_ai.csc13009.app.presentation.activity.WordDetailActivity

class DictionaryAdapter(
    private var wordModels: List<WordModel>,
    private val onItemClick: (WordModel) -> Unit // Add this callback
) : RecyclerView.Adapter<DictionaryAdapter.DictionaryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DictionaryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_word_search, parent, false)
        return DictionaryViewHolder(view)
    }

    override fun onBindViewHolder(holder: DictionaryViewHolder, position: Int) {
        holder.bind(wordModels[position])
    }

    override fun getItemCount(): Int {
        return wordModels.size
    }

    fun updateList(newWordModels: List<WordModel>) {
        wordModels = newWordModels
        notifyDataSetChanged()
    }

    inner class DictionaryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val wordTextView: TextView = itemView.findViewById(R.id.tvWord)

        fun bind(wordModel: WordModel) {
            wordTextView.text = wordModel.word

            // Thêm sự kiện click vào item
            itemView.setOnClickListener {
                onItemClick(wordModel)
            }
        }
    }
}