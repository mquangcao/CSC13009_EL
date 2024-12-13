package com.android_ai.csc13009.app.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.domain.repository.model.Word

class WordAdapter(private var words: List<Word>) : RecyclerView.Adapter<WordAdapter.WordViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_word, parent, false)
        return WordViewHolder(view)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.bind(words[position])
    }

    override fun getItemCount(): Int {
        return words.size
    }

    fun updateList(newWords: List<Word>) {
        words = newWords
        notifyDataSetChanged()
    }

    inner class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val wordTextView: TextView = itemView.findViewById(R.id.tvWord)

        fun bind(word: Word) {
            wordTextView.text = word.word
        }
    }
}
