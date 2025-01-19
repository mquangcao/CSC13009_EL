package com.android_ai.csc13009.app.presentation.receiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.text.Html
import android.text.SpannableString
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.preference.PreferenceManager
import com.android_ai.csc13009.MainActivity
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.data.local.AppDatabase
import com.android_ai.csc13009.app.data.remote.repository.FirestoreTagRepository
import com.android_ai.csc13009.app.data.repository.WordRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class WordNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        Log.d("WordNotificationReceiver", "Receiver triggered")
        // Đọc cài đặt từ SharedPreferences
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val isNotificationEnabled = preferences.getBoolean("enable_notification", true)

        if (!isNotificationEnabled) return // Nếu tắt thông báo, không làm gì cả

        // Hiển thị thông báo
        showNotification(context)
    }

    // Hiển thị thông báo
    private fun showNotification(context: Context) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val selectedTagId = preferences.getString("selected_tag_id", null)
        val selectedTagName = preferences.getString("selected_tag_name", "Tag không xác định")
        Log.d("WordNotificationReceiver", "Selected Tag ID: $selectedTagId") // Thêm ở đây

        // Tạo Intent để mở MainActivity khi nhấn vào thông báo
        val openIntent = Intent(context, MainActivity::class.java)
        openIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(
            context, 0, openIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val firestoreRepository = FirestoreTagRepository(FirebaseFirestore.getInstance())

        // Khởi tạo WordRepository
        val wordRepository = WordRepository(AppDatabase.getInstance(context).wordDao())

        var notification: NotificationCompat.Builder
        CoroutineScope(Dispatchers.IO).launch {


            if (selectedTagId.isNullOrEmpty()) {
                // Nếu chưa chọn Tag, hiển thị thông báo mặc định
                notification = NotificationCompat.Builder(context, "word_channel")
                    .setSmallIcon(R.drawable.img_thumnail_entrypoint)
                    .setContentTitle("Word for Today")
                    .setContentText("Hãy xem từ mới hôm nay của bạn!") // Nội dung mặc định
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
            } else {
                // Nếu đã chọn Tag, hiển thị danh sách từ trong Tag
                val words = try {
                    val tag = firestoreRepository.getTagById(selectedTagId)
                    if (tag != null && tag.wordIds != null) {
                        val wordList = wordRepository.getWordsByIds(tag.wordIds)
                        wordList.joinToString(", ") // Kết hợp các từ thành chuỗi
                    } else {
                        "Không có từ trong Tag"
                    }
                } catch (e: Exception) {
                    Log.e("WordNotificationReceiver", "Error fetching Tag: ${e.message}")
                    "Không thể tải từ trong Tag"
                }
                Log.d("WordNotificationReceiver", "Words from Tag: $words")

                val title = SpannableString("Từ vựng hôm nay từ tag: $selectedTagName")
                val startIndex = selectedTagName?.let { title.indexOf(it) }
                if (startIndex != -1) {
                    if (startIndex != null) {
                        title.setSpan(
                            android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                            startIndex,
                            startIndex + selectedTagName.length,
                            android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                }
                notification = NotificationCompat.Builder(context, "word_channel")
                    .setSmallIcon(R.drawable.img_thumnail_entrypoint)
                    .setContentTitle(title)
                    .setContentText("Từ trong Tag: $words") // Nội dung từ danh sách Tag
                    .setStyle(NotificationCompat.BigTextStyle().bigText(words)) // Hiển thị đầy đủ danh sách từ
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
            }

            // Hiển thị Notification

                Log.d("WordNotificationReceiver", "Displaying notification...")
                NotificationManagerCompat.from(context).notify(1, notification.build())
        }
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

            val startCalendar = Calendar.getInstance()
            val endCalendar = Calendar.getInstance()

            val (startHour, startMinute) = startTime.split(":").map { it.toInt() }
            val (endHour, endMinute) = endTime.split(":").map { it.toInt() }

            startCalendar.set(Calendar.HOUR_OF_DAY, startHour)
            startCalendar.set(Calendar.MINUTE, startMinute)
            startCalendar.set(Calendar.SECOND, 0)

            endCalendar.set(Calendar.HOUR_OF_DAY, endHour)
            endCalendar.set(Calendar.MINUTE, endMinute)
            endCalendar.set(Calendar.SECOND, 0)

            val interval = if (startCalendar.timeInMillis < endCalendar.timeInMillis) {
                ((endCalendar.timeInMillis - startCalendar.timeInMillis) / timesPerDay).toInt()
            } else {
                // Nếu `endTime` là ngày hôm sau
                ((24 * 60 * 60 * 1000 - startCalendar.timeInMillis + endCalendar.timeInMillis) / timesPerDay).toInt()
            }


            for (i in 0 until timesPerDay) {
                val notificationTime = startCalendar.timeInMillis + (interval * i)

                // Chỉ đặt lịch nếu ngày được chọn
                if (isDaySelected(selectedDays, startCalendar)) {
                    val intent = Intent(context, WordNotificationReceiver::class.java)
                    val pendingIntent = PendingIntent.getBroadcast(
                        context, i, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )

                    alarmManager.setRepeating(
                        AlarmManager.RTC_WAKEUP,
                        notificationTime,
                        interval.toLong(),
                        pendingIntent
                    )

                }
            }
        }

        fun cancelNotifications(context: Context) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            for (i in 0..10) {
                val intent = Intent(context, WordNotificationReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(
                    context, i, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                alarmManager.cancel(pendingIntent)
            }
        }

        private fun isDaySelected(selectedDays: Set<String>?, calendar: Calendar): Boolean {
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) // 1 (CN) -> 7 (T7)
            val normalizedDay = if (dayOfWeek == 1) 8 else dayOfWeek // Chuyển 1 (CN) -> 8
            Log.d("WordNotificationReceiver", "Checking day: $normalizedDay, selectedDays: $selectedDays")
            return selectedDays?.contains(normalizedDay.toString()) == true
        }

    }
}
