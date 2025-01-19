package com.android_ai.csc13009.app.presentation.activity

import android.app.TimePickerDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.preference.PreferenceManager
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.presentation.receiver.WordNotificationReceiver
import java.util.Calendar

class NotificationSettingsActivity : AppCompatActivity() {

    // Khai báo biến UI
    private lateinit var enableNotificationSwitch: SwitchCompat
    private lateinit var spinnerTimesPerDay: Spinner
    private lateinit var tvStartTime: TextView
    private lateinit var tvEndTime: TextView
    private lateinit var dayTextViews: List<TextView>

    private lateinit var sharedPreferences: SharedPreferences
    private val selectedDays = mutableSetOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_setting)

        // Khởi tạo SharedPreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        // Ánh xạ view
        enableNotificationSwitch = findViewById(R.id.switch_enable_notification)
        spinnerTimesPerDay = findViewById(R.id.spinner_times_per_day)
        tvStartTime = findViewById(R.id.tv_start_time)
        tvEndTime = findViewById(R.id.tv_end_time)

        // Danh sách các TextView cho ngày trong tuần
        dayTextViews = listOf(
            findViewById(R.id.tv_day_2),
            findViewById(R.id.tv_day_3),
            findViewById(R.id.tv_day_4),
            findViewById(R.id.tv_day_5),
            findViewById(R.id.tv_day_6),
            findViewById(R.id.tv_day_7),
            findViewById(R.id.tv_day_8)
        )
        if (dayTextViews.any { it == null }) {
            Toast.makeText(this, "Lỗi ánh xạ ngày trong tuần", Toast.LENGTH_SHORT).show()
        }

        // Đọc cài đặt từ SharedPreferences
        loadSettings()

        // Xử lý sự kiện bật/tắt thông báo
        enableNotificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("enable_notification", isChecked).apply()
            if (isChecked) {
                WordNotificationReceiver.scheduleNotifications(this) // Đặt lại thông báo
            } else {
                WordNotificationReceiver.cancelNotifications(this) // Hủy thông báo
            }
        }

        // Xử lý chọn số lần nhắc nhở trong ngày
        spinnerTimesPerDay.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                sharedPreferences.edit().putInt("times_per_day", position + 1).apply()
                WordNotificationReceiver.scheduleNotifications(this@NotificationSettingsActivity)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Xử lý chọn thời gian bắt đầu
        tvStartTime.setOnClickListener {
            showTimePickerDialog("start_time", tvStartTime)
        }

        // Xử lý chọn thời gian kết thúc
        tvEndTime.setOnClickListener {
            showTimePickerDialog("end_time", tvEndTime)
        }

        // Xử lý chọn ngày trong tuần
        dayTextViews.forEachIndexed { index, textView ->
            textView.setOnClickListener {
                toggleDaySelection(index + 2, textView) // index + 2 tương ứng thứ 2 -> CN
            }
        }
    }

    // Hiển thị hộp thoại chọn thời gian
    private fun showTimePickerDialog(key: String, textView: TextView) {
        val currentTime = Calendar.getInstance()
        val hour = currentTime.get(Calendar.HOUR_OF_DAY)
        val minute = currentTime.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            val time = String.format("%02d:%02d", selectedHour, selectedMinute)
            textView.text = time
            sharedPreferences.edit().putString(key, time).apply()
            WordNotificationReceiver.scheduleNotifications(this)
        }, hour, minute, true)

        timePickerDialog.show()
    }

    // Chọn hoặc bỏ chọn ngày trong tuần
    private fun toggleDaySelection(day: Int, textView: TextView) {
        if (selectedDays.contains(day)) {
            selectedDays.remove(day)
            textView.setBackgroundResource(R.drawable.bg_unselected) // Đổi màu khi bỏ chọn
        } else {
            selectedDays.add(day)
            textView.setBackgroundResource(R.drawable.bg_selected) // Đổi màu khi được chọn
        }

        sharedPreferences.edit().putStringSet("selected_days", selectedDays.map { it.toString() }.toSet()).apply()
        WordNotificationReceiver.scheduleNotifications(this)
    }

    // Nạp cài đặt đã lưu
    private fun loadSettings() {
        val isEnabled = sharedPreferences.getBoolean("enable_notification", true)
        val timesPerDay = sharedPreferences.getInt("times_per_day", 1)
        val startTime = sharedPreferences.getString("start_time", "07:00")
        val endTime = sharedPreferences.getString("end_time", "21:00")
        val savedDays = sharedPreferences.getStringSet("selected_days", emptySet())

        enableNotificationSwitch.isChecked = isEnabled
        spinnerTimesPerDay.setSelection(timesPerDay - 1)
        tvStartTime.text = startTime
        tvEndTime.text = endTime

        // Đánh dấu ngày đã chọn
        savedDays?.forEach { day ->
            val dayInt = day.toInt()
            selectedDays.add(dayInt)
            dayTextViews[dayInt - 2].setBackgroundResource(R.drawable.bg_selected)
        }
    }
}
