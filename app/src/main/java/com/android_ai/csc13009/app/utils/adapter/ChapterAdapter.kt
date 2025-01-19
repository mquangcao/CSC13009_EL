package com.android_ai.csc13009.app.utils.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView

import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.domain.models.Chapters
import com.android_ai.csc13009.app.presentation.activity.LearVocabActivity
import com.bumptech.glide.Glide
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import de.hdodenhof.circleimageview.CircleImageView


class ChapterAdapter(
    private val chapters: List<Chapters>
) : RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder>() {

    class ChapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgThumb = itemView.findViewById<CircleImageView>(R.id.imgThumbnail)
        val tvChapter = itemView.findViewById<TextView>(R.id.tvChapter)
        val tvVocabulary = itemView.findViewById<TextView>(R.id.tvVocabulary)
        val circularProgressBar = itemView.findViewById<CircularProgressBar>(R.id.circularProgressBar)
        val itemCard = itemView.findViewById<CardView>(R.id.itemCard)
        val txtProgress = itemView.findViewById<TextView>(R.id.txtProgress)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chapter, parent, false)
        return ChapterViewHolder(view)
    }

    override fun getItemCount(): Int = chapters.size

    override fun onBindViewHolder(holder: ChapterViewHolder, position: Int) {
        val lesson = chapters[position]

        Log.d("hi tao la quang cao ne", lesson.toString())
        Log.d("hi tao la quang cao ne", lesson.chapterName)
        Log.d("hi tao la quang cao ne", lesson.thumbnailUrl)
        Log.d("hi tao la quang cao ne", lesson.totalLesson.toString())


        Glide.with(holder.itemView.context)
            .load(lesson.thumbnailUrl)
            .placeholder(R.drawable.bell) // Ảnh hiển thị trong lúc chờ
            .error(R.drawable.auto_stories_24px) // Ảnh hiển thị khi lỗi
            .into(holder.imgThumb)

        holder.circularProgressBar.progressMax = lesson.totalLesson.toFloat()
        holder.circularProgressBar.setProgressWithAnimation(lesson.lessonFinished.toFloat())

        holder.tvChapter.text = lesson.chapterName
        holder.tvVocabulary.text = "${lesson.totalLesson} lessons"

        holder.txtProgress.text = "${ (lesson.lessonFinished.toFloat() / lesson.totalLesson.toFloat() * 100).toInt()}%"

        holder.itemCard.setOnClickListener {
            val intent = Intent(holder.itemCard.context, LearVocabActivity::class.java)
            intent.putExtra("chapterId", lesson.id)
            holder.itemCard.context.startActivity(intent)
        }
    }
}

