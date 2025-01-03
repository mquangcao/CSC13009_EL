package com.android_ai.csc13009.app.utils.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.domain.repository.model.Word
import com.android_ai.csc13009.app.presentation.activity.WordDetailActivity

class DictionaryAdapter(private var words: List<Word>) : RecyclerView.Adapter<DictionaryAdapter.DictionaryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DictionaryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_word_search, parent, false)
        return DictionaryViewHolder(view)
    }

    override fun onBindViewHolder(holder: DictionaryViewHolder, position: Int) {
        holder.bind(words[position])
    }

    override fun getItemCount(): Int {
        return words.size
    }

    fun updateList(newWords: List<Word>) {
        words = newWords
        notifyDataSetChanged()
    }

    inner class DictionaryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val wordTextView: TextView = itemView.findViewById(R.id.tvWord)

        fun bind(word: Word) {
            wordTextView.text = word.word

            // Thêm sự kiện click vào item
            itemView.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, WordDetailActivity::class.java)
                intent.putExtra("word_text", word.word) // Truyền từ cần hiển thị
                intent.putExtra("word_pronunciation", word.pronunciation)
                intent.putExtra("word_details", word.details)
                context.startActivity(intent)
            }
        }
    }
}