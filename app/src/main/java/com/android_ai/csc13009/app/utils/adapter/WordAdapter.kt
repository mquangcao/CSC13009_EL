package com.android_ai.csc13009.app.utils.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.domain.models.AnswerWord
import com.android_ai.csc13009.app.domain.models.Word
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.google.android.material.imageview.ShapeableImageView

class WordAdapter(private val words: List<AnswerWord>) : RecyclerView.Adapter<WordAdapter.WordViewHolder>()
{
    class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgWord: ShapeableImageView = itemView.findViewById(R.id.img_word)
        val tvWord: TextView = itemView.findViewById(R.id.tv_word)
        val cardWord: MaterialCardView = itemView.findViewById(R.id.card_word)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_word, parent, false)
        return WordViewHolder(view)
    }

    override fun getItemCount(): Int = words.size

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val word = words[position]
        holder.tvWord.text = word.text
        Glide.with(holder.itemView.context.applicationContext)
            .load(word.imgUrl) // URL ảnh
            .placeholder(R.drawable.bell) // Ảnh hiển thị trong lúc chờ
            .error(R.drawable.auto_stories_24px) // Ảnh hiển thị khi lỗi
            .into(holder.imgWord)

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