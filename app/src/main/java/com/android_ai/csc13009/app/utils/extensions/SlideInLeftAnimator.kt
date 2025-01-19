package com.android_ai.csc13009.app.utils.extensions

import android.animation.ObjectAnimator
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView

class SlideInLeftAnimator : DefaultItemAnimator() {
    override fun animateAdd(holder: RecyclerView.ViewHolder): Boolean {
        val itemView = holder.itemView
        itemView.translationX = -itemView.width.toFloat()
        val animator = ObjectAnimator.ofFloat(itemView, "translationX", 0f)
        animator.duration = 500 // Thời gian hiệu ứng (ms)
        animator.start()
        return super.animateAdd(holder)
    }
}