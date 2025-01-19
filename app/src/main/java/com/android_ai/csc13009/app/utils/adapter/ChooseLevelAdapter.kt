package com.android_ai.csc13009.app.utils.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.domain.models.Level
import com.android_ai.csc13009.app.utils.adapter.WordMeaningAdapter.WordMeaningViewHolder
import com.google.android.material.card.MaterialCardView







class ChooseLevelAdapter(private val listData : List<Level>) : RecyclerView.Adapter<ChooseLevelAdapter.ChooseLevelViewHolder>() {
    class ChooseLevelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvWord: TextView = itemView.findViewById(R.id.tvWord)
        val iv_icon: ImageView = itemView.findViewById(R.id.iv_icon)
        val cardWord: MaterialCardView = itemView.findViewById(R.id.card_word)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseLevelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_choose_level, parent, false)
        return ChooseLevelViewHolder(view)
    }

    override fun getItemCount(): Int = listData.size

    override fun onBindViewHolder(holder: ChooseLevelViewHolder, position: Int) {
        val level = listData[position]
        holder.iv_icon.setImageResource(level.icon)
        holder.tvWord.text = level.text

        if (level.isSelected) {
            holder.cardWord.strokeColor = holder.itemView.context.getColor(R.color.green)
        } else {
            holder.cardWord.strokeColor = holder.itemView.context.getColor(R.color.white)
        }

        holder.itemView.setOnClickListener {
            for (i in listData.indices) {
                listData[i].isSelected = i == position
            }
            notifyDataSetChanged()
        }
    }
}