package com.android_ai.csc13009.app.utils

import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.MotionEvent
import android.widget.TextView

class CustomLinkMovementMethod : LinkMovementMethod() {
    override fun onTouchEvent(widget: TextView, buffer: Spannable, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            val start = widget.selectionStart
            val end = widget.selectionEnd

            val link = buffer.getSpans(start, end, ClickableSpan::class.java)
            if (link.isNotEmpty()) {
                link[0].onClick(widget) // Kích hoạt ClickableSpan
                return true
            }
        }
        return super.onTouchEvent(widget, buffer, event) // Gọi sự kiện mặc định
    }
}