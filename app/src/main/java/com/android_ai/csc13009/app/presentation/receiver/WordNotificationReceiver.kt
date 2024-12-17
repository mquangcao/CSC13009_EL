package com.android_ai.csc13009.app.presentation.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.android_ai.csc13009.MainActivity
import com.android_ai.csc13009.R

class WordNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        // Tạo Intent để mở MainActivity khi nhấn vào thông báo
        val openIntent = Intent(context, MainActivity::class.java)
        openIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(
            context, 0, openIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Xây dựng Notification
        val notification = NotificationCompat.Builder(context, "word_channel")
            .setSmallIcon(R.drawable.ic_notification) // Thay bằng icon của bạn
            .setContentTitle("Word for Today")
            .setContentText("Hãy xem từ mới hôm nay của bạn!") // Nội dung tạm thời
            .setContentIntent(pendingIntent)
            .setAutoCancel(true) // Tự động xóa khi nhấn vào
            .build()

        // Hiển thị Notification
        NotificationManagerCompat.from(context).notify(1, notification)
    }
}
