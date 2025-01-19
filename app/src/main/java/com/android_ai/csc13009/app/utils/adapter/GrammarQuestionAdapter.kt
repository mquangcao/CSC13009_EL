import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.data.remote.model.FirestoreGrammarAnswer
import com.android_ai.csc13009.app.data.remote.model.FirestoreGrammarQuestion

class GrammarQuestionAdapter : RecyclerView.Adapter<GrammarQuestionAdapter.ViewHolder>() {

    val questionAnswerPairs = mutableListOf<Pair<FirestoreGrammarQuestion, List<FirestoreGrammarAnswer>>>()

    fun submitList(list: List<Pair<FirestoreGrammarQuestion, List<FirestoreGrammarAnswer>>>) {
        questionAnswerPairs.clear()
        questionAnswerPairs.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_grammar_question, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position + 1, questionAnswerPairs[position])  // Thêm số thứ tự cho câu hỏi
    }

    override fun getItemCount(): Int = questionAnswerPairs.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val numberText: TextView = itemView.findViewById(R.id.textNumber)  // Số thứ tự
        private val questionText: TextView = itemView.findViewById(R.id.textQuestion)
        private val answersText: TextView = itemView.findViewById(R.id.textAnswers)

        fun bind(position: Int, pair: Pair<FirestoreGrammarQuestion, List<FirestoreGrammarAnswer>>) {
            val (question, answers) = pair
            numberText.text = "$position."  // Hiển thị số thứ tự
            questionText.text = question.name
            answersText.text = answers.joinToString("\n") { answer ->
                if (answer.isCorrect) {
                    "✔ ${answer.answer}"
                } else {
                    "✘ ${answer.answer}"
                }
            }
        }
    }
}

