package com.android_ai.csc13009.app.utils.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.domain.models.GrammarAnswer

class GrammarAnswerAdapter(
    private val context: Context,
    private val answers: List<GrammarAnswer>,
    private val onAnswerSelected: (GrammarAnswer) -> Unit
) : RecyclerView.Adapter<GrammarAnswerAdapter.AnswerViewHolder>() {

    private var selectedAnswerIndex: Int = RecyclerView.NO_POSITION // Vị trí đáp án được chọn
    private var isCorrectSelected: Boolean? = null // Trạng thái đúng/sai của đáp án được chọn

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_grammar_answer, parent, false)
        return AnswerViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        val answer = answers[position]
        holder.bind(answer, position)
    }

    override fun getItemCount(): Int = answers.size

    inner class AnswerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val answerText: TextView = itemView.findViewById(R.id.answerText)
        private val cardView: CardView = itemView as CardView

        fun bind(answer: GrammarAnswer, position: Int) {
            // Hiển thị văn bản đáp án
            answerText.text = answer.answer

            // Đổi màu nền dựa vào trạng thái được chọn
            when {
                position == selectedAnswerIndex && isCorrectSelected == true -> {
                    cardView.setCardBackgroundColor(context.getColor(R.color.correctAnswerBackground))
                }
                position == selectedAnswerIndex && isCorrectSelected == false -> {
                    cardView.setCardBackgroundColor(context.getColor(R.color.incorrectAnswerBackground))
                }
                else -> {
                    cardView.setCardBackgroundColor(context.getColor(R.color.answerBackground))
                }
            }

            // Xử lý sự kiện chọn đáp án
            cardView.setOnClickListener {
                selectedAnswerIndex = position
                isCorrectSelected = answer.isCorrect

                // Gọi callback để xử lý logic bên ngoài Fragment
                onAnswerSelected(answer)

                // Làm mới toàn bộ danh sách để cập nhật trạng thái
                notifyDataSetChanged()
            }
        }
    }
}


