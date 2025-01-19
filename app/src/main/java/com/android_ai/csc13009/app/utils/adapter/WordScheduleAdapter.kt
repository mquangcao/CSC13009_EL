package com.android_ai.csc13009.app.utils.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
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
import java.util.Calendar
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
        private val tvDueBadge: TextView  = itemView.findViewById(R.id.tvDueBadge)
        private val topLayout: RelativeLayout  = itemView.findViewById(R.id.topLayout)

        fun bind(context: Context, wordSchedule: WordSchedule) {
            CoroutineScope(Dispatchers.Main).launch {
                val word = getWordById(context, wordSchedule.wordId)
                tvWord.text = word
            }

            // Check if the item is due today
            val today = Calendar.getInstance()
            val nextReviewDate = Calendar.getInstance().apply { timeInMillis = wordSchedule.nextReview }

            if (today.get(Calendar.YEAR) >= nextReviewDate.get(Calendar.YEAR) &&
                today.get(Calendar.DAY_OF_YEAR) >= nextReviewDate.get(Calendar.DAY_OF_YEAR)) {
                // Highlight due items
                tvNextReview.text = "Next Review: Today"
                tvNextReview.setTextColor(ContextCompat.getColor(context, R.color.red))
                tvDueBadge.visibility = View.VISIBLE // Show "DUE" badge
                topLayout.setBackgroundResource(R.drawable.bg_due_highlight) // Apply a special background
            } else {
                // Display the formatted date for non-due items
                val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                tvNextReview.text = "Next Review: ${dateFormat.format(Date(wordSchedule.nextReview))}"
                tvNextReview.setTextColor(ContextCompat.getColor(context, R.color.purple))
                tvDueBadge.visibility = View.GONE // Hide "DUE" badge
                topLayout.setBackgroundResource(R.drawable.bg_tag_chip)
            }

            tvReviewCount.text = "Review Count: ${wordSchedule.reviewCount}"

            // Configure swipe behavior
            swipeLayout.showMode = SwipeLayout.ShowMode.LayDown
            swipeLayout.addDrag(SwipeLayout.DragEdge.Right, itemView.findViewById(R.id.bottomLayout))

            tvDelete.setOnClickListener {
                swipeLayout.close()
                onDelete(wordSchedule)
            }

            // Close other SwipeLayouts when this one is opened
            // Close other SwipeLayouts when this one is opened
            swipeLayout.addSwipeListener(object : SwipeLayout.SwipeListener {

                override fun onOpen(layout: SwipeLayout?) {
                    // Close previously opened SwipeLayout if any
                    openedSwipeLayout?.close()
                    openedSwipeLayout = swipeLayout
                }

                override fun onClose(layout: SwipeLayout?) {
                    // Clear reference to the current SwipeLayout if it is closed
                    if (openedSwipeLayout == swipeLayout) {
                        openedSwipeLayout = null
                    }
                }

                override fun onStartOpen(layout: SwipeLayout?) {
                    // Called when the layout starts opening
                    // You can add custom logic here if needed
                }

                override fun onStartClose(layout: SwipeLayout?) {
                    // Called when the layout starts closing
                    // You can add custom logic here if needed
                }

                override fun onUpdate(layout: SwipeLayout?, leftOffset: Int, topOffset: Int) {
                    // Called when the SwipeLayout position is updated
                }

                override fun onHandRelease(layout: SwipeLayout?, xvel: Float, yvel: Float) {
                    // Called when the user releases the SwipeLayout
                }
            })

        }

    }
}


