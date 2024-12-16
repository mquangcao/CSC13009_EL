package com.android_ai.csc13009.app.utils.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView

import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.data.local.entity.ChapterEntity
import com.android_ai.csc13009.app.presentation.activity.LearVocabActivity
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import de.hdodenhof.circleimageview.CircleImageView


class ChapterAdapter(
    private val chapters: List<ChapterEntity>
) : RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder>() {

    class ChapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgThumb = itemView.findViewById<CircleImageView>(R.id.imgThumbnail)
        val tvChapter = itemView.findViewById<TextView>(R.id.tvChapter)
        val tvVocabulary = itemView.findViewById<TextView>(R.id.tvVocabulary)
        val circularProgressBar = itemView.findViewById<CircularProgressBar>(R.id.circularProgressBar)
        val itemCard = itemView.findViewById<CardView>(R.id.itemCard)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chapter, parent, false)
        return ChapterViewHolder(view)
    }

    override fun getItemCount(): Int = chapters.size

    override fun onBindViewHolder(holder: ChapterViewHolder, position: Int) {
        val lesson = chapters[position]

        holder.itemCard.setOnClickListener {
            val intent = Intent(holder.itemCard.context, LearVocabActivity::class.java)
            holder.itemCard.context.startActivity(intent)
        }
    }
}

