package com.android_ai.csc13009.app.utils.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.data.local.entity.LessonEntity

class StatisticsLessonDetailAdapter(private val lessonList: List<LessonEntity>) :
    RecyclerView.Adapter<StatisticsLessonDetailAdapter.LessonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.statistics_item_lesson, parent, false)  // Sử dụng layout tùy chỉnh
        return LessonViewHolder(view)
    }

    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        holder.bind(lessonList[position])
    }

    override fun getItemCount(): Int {
        return lessonList.size
    }

    inner class LessonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val lessonNameTextView: TextView = itemView.findViewById(R.id.textLessonName)
        private val lessonDescriptionTextView: TextView = itemView.findViewById(R.id.textLessonDescription)

        fun bind(lesson: LessonEntity) {
            lessonNameTextView.text = lesson.lessonName
            lessonDescriptionTextView.text = lesson.description
        }
    }
}