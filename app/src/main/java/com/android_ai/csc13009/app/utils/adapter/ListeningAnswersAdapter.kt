package com.android_ai.csc13009.app.utils.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.domain.models.ListeningAnswer
import com.android_ai.csc13009.app.utils.extensions.TTSSetter
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.google.android.material.imageview.ShapeableImageView

class ListeningAnswersAdapter(private val answers: List<ListeningAnswer>)
    : RecyclerView.Adapter<ListeningAnswersAdapter.ListeningAnswersViewHolder>() {

    class ListeningAnswersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvAnswerText: TextView = itemView.findViewById(R.id.tv_word)
        val imgAnswerImage: ShapeableImageView = itemView.findViewById(R.id.img_word)
        val cardAnswer: MaterialCardView = itemView.findViewById(R.id.card_word)
        val audioButton: ImageButton = itemView.findViewById(R.id.learn_question_content)
        val audioProgress: ProgressBar = itemView.findViewById(R.id.game_session_question_content_extra)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListeningAnswersViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_listening_answer, parent, false)
        return ListeningAnswersViewHolder(view)
    }

    override fun getItemCount(): Int {
        return answers.size
    }

    override fun onBindViewHolder(
        holder: ListeningAnswersViewHolder,
        position: Int
    ) {
        val answer = answers[position]
        holder.tvAnswerText.text = answer.text
        if (answer.imgUrl.isNotEmpty()) {
            holder.imgAnswerImage.visibility = View.VISIBLE
            Glide.with(holder.itemView.context)
                .load(answer.imgUrl)
                .into(holder.imgAnswerImage)
        } else {
            holder.imgAnswerImage.visibility = View.GONE
        }

        val ttsSetter = TTSSetter()
        ttsSetter.setTTS(holder.audioButton, holder.audioProgress, answer.text, holder.itemView.context)

        if (answer.isSelected) {
            holder.cardAnswer.strokeColor = holder.itemView.context.getColor(R.color.green)
        } else {
            holder.cardAnswer.strokeColor = holder.itemView.context.getColor(R.color.white)
        }

        holder.cardAnswer.setOnClickListener {
            for (i in answers.indices) {
                answers[i].isSelected = i == position
            }
            notifyDataSetChanged()
        }

    }

}