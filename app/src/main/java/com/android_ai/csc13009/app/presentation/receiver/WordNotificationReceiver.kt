package com.android_ai.csc13009.app.presentation.receiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.preference.PreferenceManager
import com.android_ai.csc13009.MainActivity
import com.android_ai.csc13009.R

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class WordNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        // Đọc cài đặt từ SharedPreferences
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val isNotificationEnabled = preferences.getBoolean("enable_notification", true)

        if (!isNotificationEnabled) return // Nếu tắt thông báo, không làm gì cả

        // Hiển thị thông báo
        showNotification(context)
    }

    // Hiển thị thông báo
    private fun showNotification(context: Context) {
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
            .setPriority(NotificationCompat.PRIORITY_HIGH) // Ưu tiên cao
            .build()

        // Hiển thị Notification
        NotificationManagerCompat.from(context).notify(1, notification)
    }

    // Đặt lịch thông báo dựa trên cài đặt
    companion object {
        fun scheduleNotifications(context: Context) {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val isNotificationEnabled = preferences.getBoolean("enable_notification", true)
            val timesPerDay = preferences.getInt("times_per_day", 1)
            val startTime = preferences.getString("start_time", "07:00") ?: "07:00"
            val endTime = preferences.getString("end_time", "21:00") ?: "21:00"
            val selectedDays = preferences.getStringSet("selected_days", emptySet())

            if (!isNotificationEnabled) {
                cancelNotifications(context)
                return
            }

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())

            val startCalendar = Calendar.getInstance()
            val endCalendar = Calendar.getInstance()

            // Thiết lập thời gian bắt đầu và kết thúc
            val (startHour, startMinute) = startTime.split(":").map { it.toInt() }
            val (endHour, endMinute) = endTime.split(":").map { it.toInt() }

            startCalendar.set(Calendar.HOUR_OF_DAY, startHour)
            startCalendar.set(Calendar.MINUTE, startMinute)
            startCalendar.set(Calendar.SECOND, 0)

            endCalendar.set(Calendar.HOUR_OF_DAY, endHour)
            endCalendar.set(Calendar.MINUTE, endMinute)
            endCalendar.set(Calendar.SECOND, 0)

            val interval = ((endCalendar.timeInMillis - startCalendar.timeInMillis) / timesPerDay).toInt()

            for (i in 0 until timesPerDay) {
                val notificationTime = startCalendar.timeInMillis + (interval * i)

                // Chỉ đặt lịch nếu ngày được chọn
                if (isDaySelected(selectedDays, startCalendar)) {
                    val intent = Intent(context, WordNotificationReceiver::class.java)
                    val pendingIntent = PendingIntent.getBroadcast(
                        context, i, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )

                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        notificationTime,
                        pendingIntent
                    )
                }
            }
        }

        // Hủy tất cả các thông báo đã lên lịch
        fun cancelNotifications(context: Context) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            for (i in 0..10) { // Giả sử có tối đa 10 thông báo
                val intent = Intent(context, WordNotificationReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(
                    context, i, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                alarmManager.cancel(pendingIntent)
            }
        }

        // Kiểm tra ngày có được chọn không
        private fun isDaySelected(selectedDays: Set<String>?, calendar: Calendar): Boolean {
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
            return selectedDays?.contains(dayOfWeek.toString()) == true
        }
    }
}
