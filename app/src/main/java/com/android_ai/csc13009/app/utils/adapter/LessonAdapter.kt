package com.android_ai.csc13009.app.utils.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.domain.models.Lesson
import com.android_ai.csc13009.app.presentation.activity.VocabularyWordActivity

class LessonAdapter (
    private val lessons: List<Lesson>
)  : RecyclerView.Adapter<LessonAdapter.LessonViewHolder>() {
    class LessonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.card_lesson)
        val imageView: ImageView = itemView.findViewById(R.id.item_image)
        val titleView: TextView = itemView.findViewById(R.id.tv_title)
        val questionView: TextView = itemView.findViewById(R.id.tv_question)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lesson_item_layout, parent, false)
        return LessonViewHolder(view)
    }

    override fun getItemCount(): Int = lessons.size

    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        val lesson = lessons[position]
        holder.imageView.setImageResource(lesson.imageRes)
        holder.titleView.text = lesson.title
        holder.questionView.text = "${lesson.questionCount} Questions"

        if (lesson.isUnlocked) {
            holder.cardView.alpha = 1.0f
            holder.cardView.isClickable = true
        } else {
            holder.cardView.alpha = 0.5f
            holder.cardView.isClickable = false
        }

        holder.cardView.setOnClickListener {
            val intent = Intent(holder.cardView.context, VocabularyWordActivity::class.java)
            holder.cardView.context.startActivity(intent)
        }
    }
}