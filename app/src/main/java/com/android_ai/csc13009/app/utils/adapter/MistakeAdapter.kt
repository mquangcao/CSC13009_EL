package com.android_ai.csc13009.app.utils.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.data.remote.model.FirestoreQuestion
import com.android_ai.csc13009.app.domain.models.GrammarQuestion
import com.android_ai.csc13009.app.domain.models.ListeningQuestion
import com.android_ai.csc13009.app.domain.models.Question
import com.android_ai.csc13009.app.presentation.activity.GrammarMistakePracticeActivity
import com.android_ai.csc13009.app.presentation.activity.ListeningMistakePracticeActivity
import com.android_ai.csc13009.app.presentation.activity.MistakesActivity
import com.android_ai.csc13009.app.presentation.activity.VocabMistakePracticeActivity


class MistakeAdapter(
    private var grammarQuestions: List<GrammarQuestion>,
    private var vocabQuestions: List<Question>,
    private var listeningQuestions: List<ListeningQuestion>,
    private val activityResultLauncher: ActivityResultLauncher<Intent>
) : RecyclerView.Adapter<MistakeAdapter.MistakeViewHolder>() {

    private var allQuestions: List<Any> = buildList {
        addAll(grammarQuestions)
        addAll(vocabQuestions)
        addAll(listeningQuestions)
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
            is Question -> {
                holder.tvInstruction.text = "Vocabulary Question"
                holder.tvMistake.text = question.question
            }
            is ListeningQuestion -> {
                holder.tvInstruction.text = "Listening Question"
                holder.tvMistake.text = "Listen and choose the correct answer"
            }
        }

        holder.ivIcon.setImageResource(R.drawable.ic_heart_broken)

        holder.itemView.setOnClickListener {
            when (question) {
                is GrammarQuestion -> {
                    val context = holder.itemView.context
                    val intent = Intent(context, GrammarMistakePracticeActivity::class.java)
                    intent.putExtra("question", question)
                    activityResultLauncher.launch(intent) // Use the launcher
                }
                is Question -> {
                    val context = holder.itemView.context
                    val intent = Intent(context, VocabMistakePracticeActivity::class.java)
                    intent.putExtra("question", question)
                    activityResultLauncher.launch(intent) // Use the launcher
                }
                is ListeningQuestion -> {
                    val context = holder.itemView.context
                    val intent = Intent(context, ListeningMistakePracticeActivity::class.java)
                    intent.putExtra("question", question)
                    activityResultLauncher.launch(intent) // Use the launcher
                }
            }
        }
    }


    override fun getItemCount(): Int = allQuestions.size

    fun updateData(newGrammarQuestions: List<GrammarQuestion>, newVocabQuestions: List<Question>, newListeningQuestions: List<ListeningQuestion>) {
        this.grammarQuestions = newGrammarQuestions
        this.vocabQuestions = newVocabQuestions
        this.listeningQuestions = newListeningQuestions

        allQuestions = buildList {
            addAll(grammarQuestions)
            addAll(vocabQuestions)
            addAll(listeningQuestions)
        }
        notifyDataSetChanged() // Notify the adapter of data changes
    }
}
