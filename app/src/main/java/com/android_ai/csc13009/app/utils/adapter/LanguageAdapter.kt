package com.android_ai.csc13009.app.utils.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.domain.models.Language
import de.hdodenhof.circleimageview.CircleImageView

class LanguageAdapter(var lang: List<Language>) : RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder>() {
    var onItemClick: ((String) -> Unit)? = null

    class LanguageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgLanguage: CircleImageView = itemView.findViewById(R.id.imgLanguage)
        val tvLanguage: TextView = itemView.findViewById(R.id.tvLanguage)
        val imgCheck: ImageView = itemView.findViewById(R.id.imgCheck)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.language_item, parent, false)
        return LanguageViewHolder(view)
    }

    override fun getItemCount(): Int = lang.size

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        val lang = lang[position]
        holder.imgLanguage.setImageResource(lang.id)
        holder.tvLanguage.text = lang.name

        if (lang.isSelected) {
            holder.imgCheck.visibility = View.VISIBLE
        } else {
            holder.imgCheck.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            for (i in this.lang.indices) {
                this.lang[i].isSelected = i == position
            }
            notifyDataSetChanged()
            onItemClick?.invoke(this.lang[position].code)
        }
    }


}