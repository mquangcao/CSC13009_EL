package com.android_ai.csc13009.app.utils.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.data.local.AppDatabase
import com.android_ai.csc13009.app.data.repository.WordRepository
import com.android_ai.csc13009.app.domain.models.WordSchedule
import com.daimajia.swipe.SwipeLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WordScheduleAdapter(
    private val context: Context,
    private var wordSchedules: List<WordSchedule>, // Assuming WordSchedule is your data class
    private val onDelete: (WordSchedule) -> Unit
) : RecyclerView.Adapter<WordScheduleAdapter.WordScheduleViewHolder>() {

    private var openedSwipeLayout: SwipeLayout? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordScheduleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_word_schedule, parent, false)
        return WordScheduleViewHolder(view)
    }

    override fun onBindViewHolder(holder: WordScheduleViewHolder, position: Int) {
        val wordSchedule = wordSchedules[position]
        holder.bind(context, wordSchedule)
    }

    override fun getItemCount(): Int = wordSchedules.size

    fun currentList(): List<WordSchedule> {
        return wordSchedules
    }

    fun updateList(newWordSchedules: List<WordSchedule>) {
        wordSchedules = newWordSchedules
        notifyDataSetChanged()
    }

    suspend fun getWordById(context: Context, wordId: Int): String {
        // Initialize Room Database and DAO
        val database = AppDatabase.getInstance(context)
        val wordDao = database.wordDao()
        val wordRepository = WordRepository(wordDao)

        // Fetch the word from the repository
        return wordRepository.getWordById(wordId)?.word ?: "Unknown Word"
    }


    inner class WordScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val swipeLayout: SwipeLayout = itemView.findViewById(R.id.swipeLayout)
        private val tvWord: TextView = itemView.findViewById(R.id.tvWord)
        private val tvNextReview: TextView = itemView.findViewById(R.id.tvNextReview)
        private val tvReviewCount: TextView = itemView.findViewById(R.id.tvReviewCount)
        //private val pbReviewProgress: ProgressBar = itemView.findViewById(R.id.pbReviewProgress)
        private val tvDelete: TextView = itemView.findViewById(R.id.tvDelete)
        private var isSwiping = false // Trạng thái để theo dõi swipe

        fun bind(context: Context, wordSchedule: WordSchedule) {
            CoroutineScope(Dispatchers.Main).launch {
                val word = getWordById(context, wordSchedule.wordId)
                tvWord.text = word
            }
            // Convert the nextReview timestamp to a human-readable date format
            val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            val nextReviewDate = Date(wordSchedule.nextReview)
            tvNextReview.text = "Next Review: ${dateFormat.format(nextReviewDate)}"
            tvReviewCount.text = "Review Count: ${wordSchedule.reviewCount}"

            // Configure swipe behavior
            swipeLayout.showMode = SwipeLayout.ShowMode.LayDown
            swipeLayout.addDrag(SwipeLayout.DragEdge.Right, itemView.findViewById(R.id.bottomLayout))

            tvDelete.setOnClickListener {
                swipeLayout.close()
                onDelete(wordSchedule)
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
                    itemView.postDelayed({
                        isSwiping = false
                    }, 200)
                }

                override fun onStartClose(layout: SwipeLayout?) {
                    isSwiping = true
                }

                override fun onUpdate(layout: SwipeLayout?, leftOffset: Int, topOffset: Int) {}

                override fun onHandRelease(layout: SwipeLayout?, xvel: Float, yvel: Float) {
                    //isSwiping = false
                }
            })
        }
    }
}


