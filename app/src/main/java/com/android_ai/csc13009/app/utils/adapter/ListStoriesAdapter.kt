package com.android_ai.csc13009.app.utils.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.domain.models.Story
import com.android_ai.csc13009.app.presentation.activity.StoryActivity
import com.android_ai.csc13009.app.utils.adapter.WordAdapter.WordViewHolder
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.google.android.material.imageview.ShapeableImageView

class ListStoriesAdapter(private val stories: List<Story>) : RecyclerView.Adapter<ListStoriesAdapter.ListStoriesHolder>() {
    class ListStoriesHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgWord: ShapeableImageView = itemView.findViewById(R.id.img_word)
        val tvWord: TextView = itemView.findViewById(R.id.tv_word)
        val cardWord: MaterialCardView = itemView.findViewById(R.id.card_word)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListStoriesHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_word, parent, false)
        return ListStoriesHolder(view)
    }

    override fun getItemCount(): Int = stories.size

    override fun onBindViewHolder(holder: ListStoriesHolder, position: Int) {
        val story = stories[position]
        holder.tvWord.text = story.title

        Glide.with(holder.itemView.context.applicationContext)
            .load(story.imgUrl) // URL ảnh
            .into(holder.imgWord)

        holder.cardWord.setOnClickListener {
            val intent = Intent(holder.itemView.context, StoryActivity::class.java)
            intent.putExtra("storyId", story.id)
            holder.itemView.context.startActivity(intent)
        }
    }
}