package com.android_ai.csc13009.app.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.data.local.entity.Conversation
import com.android_ai.csc13009.app.domain.models.StoryItem
import com.android_ai.csc13009.app.utils.adapter.StoryAdapter
import com.android_ai.csc13009.app.utils.extensions.SlideInLeftAnimator
import com.google.android.material.button.MaterialButton

class StoryFragment(private val conversations : List<Conversation>) : Fragment() {
    private lateinit var buttonAddMessage: MaterialButton
    private lateinit var recyclerView : RecyclerView
    private lateinit var storyAdapter: StoryAdapter

    private val pendingItems = mutableListOf<StoryItem>()
    private val items = mutableListOf<StoryItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_story, container, false)
        buttonAddMessage = view.findViewById(R.id.buttonAddMessage)
        recyclerView = view.findViewById(R.id.recyclerView)
        storyAdapter = StoryAdapter(items)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = storyAdapter
        recyclerView.itemAnimator = SlideInLeftAnimator()

        prepareStory()

        showNextItem()
        buttonAddMessage.setOnClickListener {
            showNextItem()
        }


        return view
    }

    private fun showNextItem() {
        if (pendingItems.isNotEmpty()) {
            val nextItem = pendingItems.removeAt(0) // Lấy mục đầu tiên từ danh sách chờ
            recyclerView.postDelayed({

                // Nếu không phải câu hỏi, thêm trực tiếp vào danh sách hiển thị
                items.add(nextItem)
                storyAdapter.notifyItemInserted(items.size - 1) // Thông báo RecyclerView
                recyclerView.scrollToPosition(items.size - 1) // Cuộn đến mục mới

            }, 100) // Delay 300ms giữa các mục
        } else {
            val result = Bundle()
            result.apply {
                putString("story", "completed")
            }
            parentFragmentManager.setFragmentResult("taskCompleted", result)
        }
    }

    private fun prepareStory() {
        conversations.forEach { conversation ->
            if(conversation.type == "narration") {
                pendingItems.add(StoryItem.Narration(conversation.message))
            } else {
                pendingItems.add(StoryItem.Message(conversation.message,conversation.gender))
            }
        }
    }
}