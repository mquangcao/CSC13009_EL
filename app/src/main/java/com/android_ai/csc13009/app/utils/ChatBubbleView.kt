package com.android_ai.csc13009.app.utils

import android.content.Context
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.text.set
import com.android_ai.csc13009.R

class ChatBubbleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val chatTextView: TextView

    init {
        inflate(context, R.layout.chat_bubble_view, this)
        chatTextView = findViewById(R.id.chat_text_view)

        // Đọc các thuộc tính tùy chỉnh
        context.theme.obtainStyledAttributes(attrs, R.styleable.ChatBubbleView, 0, 0).apply {
            try {
                // Chiều rộng và chiều cao của toàn bộ ChatBubbleView
                val chatWidth = getLayoutDimension(R.styleable.ChatBubbleView_chatWidth, LayoutParams.WRAP_CONTENT)
                val chatHeight = getLayoutDimension(R.styleable.ChatBubbleView_chatHeight, LayoutParams.WRAP_CONTENT)

                val params = LayoutParams(chatWidth, chatHeight)
                this@ChatBubbleView.layoutParams = params

                // Kích thước chữ
                val textSize = getDimension(R.styleable.ChatBubbleView_chatTextSize, 16f)
                chatTextView.textSize = textSize

                // Màu chữ
                val textColor = getColor(R.styleable.ChatBubbleView_chatTextColor, android.graphics.Color.BLACK)
                chatTextView.setTextColor(textColor)

                // Nền
                val background = getDrawable(R.styleable.ChatBubbleView_chatBackground)
                if (background != null) {
                    this@ChatBubbleView.background = background
                }

                // Padding
                val padding = getDimensionPixelSize(R.styleable.ChatBubbleView_chatPadding, 0)
                setPadding(padding, padding, padding, padding)
            } finally {
                recycle()
            }
        }
    }

    // Hàm để đặt nội dung cho ô chat
    fun setText(content: String) {
        val spannableString = SpannableString(content)
        val words = content.split(" ")

        var startIndex = 0
        for (word in words) {
            val endIndex = startIndex + word.length
            spannableString[startIndex..endIndex] = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    Toast.makeText(context, word, Toast.LENGTH_SHORT).show()
                }

                override fun updateDrawState(ds: android.text.TextPaint) {
                    // Loại bỏ gạch chân
                    ds.isUnderlineText = false
                    // Đặt màu chữ thành màu đen
                    ds.color = android.graphics.Color.BLACK
                }

            }
            startIndex = endIndex + 1 // +1 cho khoảng cách giữa các từ
        }

        chatTextView.text = spannableString
        chatTextView.movementMethod = CustomLinkMovementMethod() // Sử dụng lớp tùy chỉnh
        chatTextView.highlightColor = android.graphics.Color.TRANSPARENT // Loại bỏ highlight
    }


}