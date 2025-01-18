package com.android_ai.csc13009.app.utils.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.domain.models.ListeningTopics
import com.android_ai.csc13009.app.presentation.activity.LearVocabActivity
import com.android_ai.csc13009.app.presentation.activity.LearnListeningActivity
import com.bumptech.glide.Glide
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import de.hdodenhof.circleimageview.CircleImageView

class ListeningTopicAdapter( private val listeningTopics: List<ListeningTopics>)
    : RecyclerView.Adapter<ListeningTopicAdapter.ListeningTopicViewHolder>() {
    class ListeningTopicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgThumb = itemView.findViewById<CircleImageView>(R.id.imgThumbnail)
        val tvChapter = itemView.findViewById<TextView>(R.id.tvChapter)
        val tvVocabulary = itemView.findViewById<TextView>(R.id.tvVocabulary)
        val circularProgressBar = itemView.findViewById<CircularProgressBar>(R.id.circularProgressBar)
        val itemCard = itemView.findViewById<CardView>(R.id.itemCard)
        val txtProgress = itemView.findViewById<TextView>(R.id.txtProgress)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListeningTopicViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chapter, parent, false)
        return ListeningTopicViewHolder(view)
    }

    override fun getItemCount(): Int = listeningTopics.size

    override fun onBindViewHolder(holder: ListeningTopicViewHolder, position: Int) {
        val topic = listeningTopics[position]

        Glide.with(holder.itemView.context)
            .load(topic.thumbnailUrl)
            .into(holder.imgThumb)

        holder.circularProgressBar.progressMax = topic.totalLesson.toFloat()
        holder.circularProgressBar.setProgressWithAnimation(topic.lessonFinished.toFloat())

        holder.tvChapter.text = topic.chapterName
        holder.tvVocabulary.text = "${topic.totalLesson} lessons"

        holder.txtProgress.text = "${ (topic.lessonFinished.toFloat() / topic.totalLesson.toFloat() * 100).toInt()}%"

        holder.itemCard.setOnClickListener {
            val intent = Intent(holder.itemCard.context, LearnListeningActivity::class.java)
            intent.putExtra("topicId", topic.id)
            holder.itemCard.context.startActivity(intent)
        }
    }
}