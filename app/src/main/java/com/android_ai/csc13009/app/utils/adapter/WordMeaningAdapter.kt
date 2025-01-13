package com.android_ai.csc13009.app.utils.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.domain.models.AnswerWord
import com.google.android.material.card.MaterialCardView


class WordMeaningAdapter(private val words: List<AnswerWord>) : RecyclerView.Adapter<WordMeaningAdapter.WordMeaningViewHolder>() {
    class WordMeaningViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvWord: TextView = itemView.findViewById(R.id.tvWord)
        val cardWord: MaterialCardView = itemView.findViewById(R.id.card_word)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordMeaningViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_word_meaning, parent, false)
        return WordMeaningViewHolder(view)
    }

    override fun getItemCount(): Int = words.size

    override fun onBindViewHolder(holder: WordMeaningViewHolder, position: Int) {
        val word = words[position]
        holder.tvWord.text = word.text

        if (word.isSelected) {
            holder.cardWord.strokeColor = holder.itemView.context.getColor(R.color.green)
        } else {
            holder.cardWord.strokeColor = holder.itemView.context.getColor(R.color.white)
        }

        holder.cardWord.setOnClickListener {
            for (i in words.indices) {
                words[i].isSelected = i == position
            }
            notifyDataSetChanged()
        }
    }
}