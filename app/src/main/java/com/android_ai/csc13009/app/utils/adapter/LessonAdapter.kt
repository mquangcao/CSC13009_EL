package com.android_ai.csc13009.app.utils.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
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
        val titleView: TextView = itemView.findViewById(R.id.tv_title)
        val rlLesson = itemView.findViewById<RelativeLayout>(R.id.rl_lesson)
        val txtProgress = itemView.findViewById<TextView>(R.id.tv_progress)
        val ivStart = itemView.findViewById<ImageView>(R.id.iv_star)
        val progressBar = itemView.findViewById<ProgressBar>(R.id.game_session_question_content_extra)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lesson_item_layout, parent, false)
        return LessonViewHolder(view)
    }

    override fun getItemCount(): Int = lessons.size

    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        val lesson = lessons[position]

        holder.titleView.text = lesson.lessonName

        if(lesson.isOpen) {
            holder.cardView.setOnClickListener {
                val intent = Intent(holder.cardView.context, VocabularyWordActivity::class.java)
                intent.putExtra("question", ArrayList(lesson.questions))
                intent.putExtra("lessonId", lesson.id)
                holder.cardView.context.startActivity(intent)
            }
        } else {
            holder.rlLesson.alpha = 0.5f
            holder.ivStart.visibility = View.GONE
        }

        holder.progressBar.max = lesson.totalQuestion
        holder.progressBar.progress = lesson.questionSuccess

        holder.txtProgress.text = "${lesson.questionSuccess}/${lesson.totalQuestion}"
    }
}