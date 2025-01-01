package com.android_ai.csc13009.app.utils.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.domain.models.WordModel
import com.android_ai.csc13009.app.presentation.activity.WordDetailActivity
import com.daimajia.swipe.SwipeLayout

class WordListAdapter(
    private var words: List<WordModel>,
    private val onDeleteClicked: (WordModel) -> Unit
) : RecyclerView.Adapter<WordListAdapter.WordViewHolder>() {

    private var openedSwipeLayout: SwipeLayout? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_word_tag, parent, false)
        return WordViewHolder(view)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.bind(words[position])
    }

    override fun getItemCount(): Int {
        return words.size
    }

    fun updateList(newWords: List<WordModel>) {
        words = newWords
        notifyDataSetChanged()
    }

    inner class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val swipeLayout: SwipeLayout = itemView.findViewById(R.id.swipeLayout)
        private val wordTextView: TextView = itemView.findViewById(R.id.tvWord)
        private val deleteButton: TextView = itemView.findViewById(R.id.tvDelete)
        private var isSwiping = false // Trạng thái để theo dõi swipe

        fun bind(wordModel: WordModel) {
            wordTextView.text = wordModel.word

            // Configure swipe behavior
            swipeLayout.showMode = SwipeLayout.ShowMode.LayDown
            swipeLayout.addDrag(SwipeLayout.DragEdge.Right, itemView.findViewById(R.id.bottomLayout))

            // Handle delete button click
            deleteButton.setOnClickListener {
                onDeleteClicked(wordModel)
                swipeLayout.close()
            }

            // Close other SwipeLayouts when this one is opened
            swipeLayout.addSwipeListener(object : SwipeLayout.SwipeListener {

                override fun onOpen(layout: SwipeLayout?) {
                    openedSwipeLayout?.close()
                    openedSwipeLayout = swipeLayout
                    isSwiping = false
                }

                override fun onStartOpen(layout: SwipeLayout?) {
                    isSwiping = true
                }

                override fun onClose(layout: SwipeLayout?) {
                    if (openedSwipeLayout == swipeLayout) {
                        openedSwipeLayout = null
                    }
                    isSwiping = false
                }

                override fun onStartClose(layout: SwipeLayout?) {
                    isSwiping = true
                }

                override fun onUpdate(layout: SwipeLayout?, leftOffset: Int, topOffset: Int) {}

                override fun onHandRelease(layout: SwipeLayout?, xvel: Float, yvel: Float) {
                    //isSwiping = false
                }
            })

            // Add click listener to the item
            itemView.setOnClickListener {
                if (!isSwiping && swipeLayout.openStatus == SwipeLayout.Status.Close) {
                    val context = itemView.context
                    val intent = Intent(context, WordDetailActivity::class.java).apply {
                        putExtra("word_id", wordModel.id)
                        putExtra("word_text", wordModel.word)
                        putExtra("word_pronunciation", wordModel.pronunciation)
                        putExtra("word_details", wordModel.details)
                    }
                    context.startActivity(intent)
                }
            }
        }
    }
}
