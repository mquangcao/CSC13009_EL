package com.android_ai.csc13009.app.utils.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.domain.models.Tag

class TagAdapter(private val onTagClicked: (Tag) -> Unit, private val onDeleteClicked: (Tag) -> Unit) :
    RecyclerView.Adapter<TagAdapter.TagViewHolder>() {

    private val tags = mutableListOf<Tag>()

    fun submitList(newTags: List<Tag>) {
        tags.clear()
        tags.addAll(newTags)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tag, parent, false)
        return TagViewHolder(view)
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        holder.bind(tags[position])
    }

    override fun getItemCount(): Int = tags.size

    inner class TagViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTagName: TextView = itemView.findViewById(R.id.tvTagName)
        private val ivDeleteTag: ImageView = itemView.findViewById(R.id.ivDeleteTag)

        fun bind(tag: Tag) {
            tvTagName.text = tag.name
            itemView.setOnClickListener { onTagClicked(tag) }
            ivDeleteTag.setOnClickListener { onDeleteClicked(tag) }
        }
    }
}