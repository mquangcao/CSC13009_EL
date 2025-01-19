import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.data.remote.model.FirestoreGrammarAnswer
import com.android_ai.csc13009.app.data.remote.model.FirestoreGrammarQuestion

class ExportGrammarQuestionAdapter : RecyclerView.Adapter<ExportGrammarQuestionAdapter.ViewHolder>() {

    val questionAnswerPairs = mutableListOf<Pair<FirestoreGrammarQuestion, List<FirestoreGrammarAnswer>>>()

    fun submitList(list: List<Pair<FirestoreGrammarQuestion, List<FirestoreGrammarAnswer>>>) {
        questionAnswerPairs.clear()
        questionAnswerPairs.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_grammar_question_export, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position + 1, questionAnswerPairs[position])  // Thêm số thứ tự cho câu hỏi
    }

    override fun getItemCount(): Int = questionAnswerPairs.size

    // Phương thức này sẽ tạo itemView cho một vị trí và trả về nó
    fun getItemViewAt(position: Int, parent: ViewGroup): View {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_grammar_question_export, parent, false)
        val holder = ViewHolder(view)
        holder.bind(position + 1, questionAnswerPairs[position])
        return holder.itemView
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val numberText: TextView = itemView.findViewById(R.id.textNumber)  // Số thứ tự
        private val questionText: TextView = itemView.findViewById(R.id.textQuestion)
        private val answersContainer: LinearLayout = itemView.findViewById(R.id.answersContainer)

        fun bind(position: Int, pair: Pair<FirestoreGrammarQuestion, List<FirestoreGrammarAnswer>>) {
            val (question, answers) = pair
            numberText.text = "$position."  // Hiển thị số thứ tự
            questionText.text = question.name

            // Xóa đáp án cũ (nếu có)
            answersContainer.removeAllViews()

            // Thêm các đáp án mới
            answers.forEach { answer ->
                val answerView = LayoutInflater.from(itemView.context)
                    .inflate(R.layout.item_answers_export, answersContainer, false)

                val iconView: ImageView = answerView.findViewById(R.id.iconAnswer)
                val textView: TextView = answerView.findViewById(R.id.textAnswer)

                // Cập nhật biểu tượng và nội dung
                iconView.setImageResource(
                    if (answer.isCorrect) R.drawable.ic_check else R.drawable.ic_cross
                )
                textView.text = answer.answer

                // Thêm vào container
                answersContainer.addView(answerView)
            }
        }
    }
}


