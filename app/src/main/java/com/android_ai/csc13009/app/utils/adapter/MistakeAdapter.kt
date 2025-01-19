package com.android_ai.csc13009.app.utils.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.data.remote.model.FirestoreQuestion
import com.android_ai.csc13009.app.domain.models.GrammarQuestion
import com.android_ai.csc13009.app.domain.models.Question


class MistakeAdapter(
    private val grammarQuestions: List<GrammarQuestion>,
    private val vocabQuestions: List<FirestoreQuestion>,
    //private val listeningQuestions: List<ListeningQuestion>
) : RecyclerView.Adapter<MistakeAdapter.MistakeViewHolder>() {

    private val allQuestions: List<Any> = buildList {
        addAll(grammarQuestions)
        addAll(vocabQuestions)
        //addAll(listeningQuestions)
    }

    inner class MistakeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvInstruction: TextView = itemView.findViewById(R.id.tvInstruction)
        val tvMistake: TextView = itemView.findViewById(R.id.tvMistake)
        val ivIcon: ImageView = itemView.findViewById(R.id.ivIcon)
        val ivStatus: ImageView = itemView.findViewById(R.id.ivStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MistakeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mistake, parent, false)
        return MistakeViewHolder(view)
    }

    override fun onBindViewHolder(holder: MistakeViewHolder, position: Int) {
        val question = allQuestions[position]

        when (question) {
            is GrammarQuestion -> {
                holder.tvInstruction.text = "Grammar Question"
                holder.tvMistake.text = question.name
            }
            is FirestoreQuestion -> {
                holder.tvInstruction.text = "Vocabulary Question"
                holder.tvMistake.text = question.question
            }
//            is ListeningQuestion -> {
//                holder.tvInstruction.text = "Listening Question"
//                holder.tvMistake.text = question.question
//            }
        }

        holder.ivIcon.setImageResource(R.drawable.ic_heart_broken)
    }


    override fun getItemCount(): Int = allQuestions.size
}
