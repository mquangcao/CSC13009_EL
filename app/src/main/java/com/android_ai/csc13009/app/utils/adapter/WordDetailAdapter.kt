package com.android_ai.csc13009.app.utils.adapter

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.domain.models.WordDetailItem

class WordDetailAdapter(private val items: List<WordDetailItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_TYPE = 0 // Loại từ: *
        const val TYPE_MEANING = 1 // Nghĩa tiếng Việt: -
        const val TYPE_EXAMPLE = 2 // Ví dụ: =
        const val TYPE_EXAMPLE_MEANING = 3 // Nghĩa ví dụ: +
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            TYPE_TYPE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_word_type, parent, false)
                WordTypeViewHolder(view)
            }
            TYPE_MEANING -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_meaning, parent, false)
                MeaningViewHolder(view)
            }
            TYPE_EXAMPLE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_example, parent, false)
                ExampleViewHolder(view)
            }
            TYPE_EXAMPLE_MEANING -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_example_meaning, parent, false)
                ExampleMeaningViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when(holder) {
            is WordTypeViewHolder -> holder.bind(item.content)
            is MeaningViewHolder -> holder.bind(item.content)
            is ExampleViewHolder -> holder.bind(item.content)
            is ExampleMeaningViewHolder -> holder.bind(item.content)
        }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return when (items[position].type) {
            "*" -> TYPE_TYPE
            "-" -> TYPE_MEANING
            "=" -> TYPE_EXAMPLE
            "+" -> TYPE_EXAMPLE_MEANING
            else -> throw IllegalArgumentException("Unknown type")
        }
    }

    // ViewHolder for each type

    class WordTypeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val typeTextView: TextView = itemView.findViewById(R.id.tvWordType)

        fun bind(content: String) {
            typeTextView.text = Html.fromHtml("<u>$content</u>")
        }
    }

    class MeaningViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val meaningTextView: TextView = itemView.findViewById(R.id.tvMeaning)

        fun bind(content: String) {
            meaningTextView.text = content
        }
    }

    class ExampleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val exampleTextView: TextView = itemView.findViewById(R.id.tvExample)

        fun bind(content: String) {
            exampleTextView.text = content
        }
    }

    class ExampleMeaningViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val exampleMeaningTextView: TextView = itemView.findViewById(R.id.tvExampleMeaning)

        fun bind(content: String) {
            exampleMeaningTextView.text = content
        }
    }
}
