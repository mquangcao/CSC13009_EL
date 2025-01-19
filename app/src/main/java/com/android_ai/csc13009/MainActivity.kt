package com.android_ai.csc13009

import com.android_ai.csc13009.app.presentation.receiver.WordNotificationReceiver
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android_ai.csc13009.app.presentation.activity.DashboardActivity
import java.util.Calendar
import android.Manifest
import android.util.Log
import androidx.core.app.ActivityOptionsCompat
import com.android_ai.csc13009.app.presentation.activity.IntroActivity
import com.android_ai.csc13009.app.presentation.activity.LoginActivity
import com.android_ai.csc13009.app.presentation.activity.StreakActivity
import com.android_ai.csc13009.app.presentation.receiver.WordNotificationReceiver.Companion.scheduleNotifications
import com.android_ai.csc13009.app.utils.extensions.LocaleUtils
import com.google.firebase.auth.FirebaseAuth


@Suppress("DEPRECATION")


class MainActivity : AppCompatActivity() {

    // Đăng ký yêu cầu quyền POST_NOTIFICATIONS
    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                scheduleNotifications(this) // Lên lịch thông báo khi có quyền
            } else {
                showPermissionDeniedMessage()
            }
        }

    lateinit var topAnim : Animation
    lateinit var bottomAnim : Animation
    lateinit var imageView : ImageView
    lateinit var logo : TextView
    lateinit var slogan : TextView

    companion object {
        const val SPLASH_SCREEN = 3500
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Tạo Notification Channel (Android 8+)
        createNotificationChannel()

        // Kiểm tra và yêu cầu quyền (nếu cần)
        checkAndRequestNotificationPermission()

        // Lên lịch thông báo dựa trên cài đặt (nếu đã cấp quyền)
        scheduleNotifications(this)



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)

        // hooks
        imageView = findViewById(R.id.imageView)
        logo = findViewById(R.id.textView)
        slogan = findViewById(R.id.textView2)


        imageView.startAnimation(topAnim)
        logo.startAnimation(bottomAnim)
        slogan.startAnimation(bottomAnim)

        Handler().postDelayed({
            //changeFragment()
            //val intent = Intent(this, LoginActivity::class.java)
            //val intent = Intent(this, DashboardActivity::class.java)

            val pairs = listOf(
                Pair(imageView, "logo_image"),
                Pair(logo, "logo_text"),
            )

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, *pairs.map { androidx.core.util.Pair(it.first, it.second) }.toTypedArray())

            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser != null) {
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                /*val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent, options.toBundle())*/
            } else {
//                val intent = Intent(this, LoginActivity::class.java)
                val intent = Intent(this, IntroActivity::class.java)
                startActivity(intent, options.toBundle())
            }

            finish()
        }, SPLASH_SCREEN.toLong())

        val intent = Intent(this, WordNotificationReceiver::class.java)
        sendBroadcast(intent)
    }

    private fun changeFragment() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "word_channel",
                "Word For Today Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Channel for daily word notifications"
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                scheduleNotifications(this) // Lên lịch thông báo ngay nếu đã có quyền
            }
        } else {
            // Android 12 trở xuống không cần quyền đặc biệt
            scheduleNotifications(this)
        }
    }

    /*private fun setDailyNotification() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, WordNotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Đặt giờ cố định cho thông báo (ví dụ: 8:00 AM)
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 19)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        // Nếu đã quá thời gian hiện tại, đặt cho ngày hôm sau
        if (calendar.timeInMillis < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        // Đặt Alarm để lặp lại hàng ngày
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
        Log.d("AlarmManager", "Notification set for: ${calendar.time}")

    }*/

    private fun showPermissionDeniedMessage() {
        // Hiển thị thông báo nếu người dùng từ chối cấp quyền
        Toast.makeText(
            this,
            "Ứng dụng cần quyền thông báo để gửi nhắc nhở hàng ngày.",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun attachBaseContext(newBase: Context?) {
        val context = newBase

        val savedLocale = LocaleUtils.getLocale(newBase as Context)

        context!!.resources.configuration.setLocale(savedLocale)

        super.attachBaseContext(newBase)
    }

//    private fun changeFragment() {
//        val intent = Intent(this, DashboardActivity::class.java)
//        startActivity(intent)
//        finish()
//    }

}