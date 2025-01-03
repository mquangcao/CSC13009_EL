package com.android_ai.csc13009

import com.android_ai.csc13009.app.presentation.receiver.WordNotificationReceiver
import android.app.NotificationChannel
import android.app.NotificationManager
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
import com.android_ai.csc13009.app.presentation.receiver.WordNotificationReceiver.Companion.scheduleNotifications
import android.Manifest
import android.content.Intent
import androidx.core.app.ActivityOptionsCompat
import androidx.preference.PreferenceManager

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    // Đăng ký yêu cầu quyền POST_NOTIFICATIONS cho Android 13+
    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                scheduleNotifications(this) // Lên lịch thông báo khi có quyền
            } else {
                showPermissionDeniedMessage()
            }
        }

    // Khai báo biến cho hiệu ứng Splash
    private lateinit var topAnim: Animation
    private lateinit var bottomAnim: Animation
    private lateinit var imageView: ImageView
    private lateinit var logo: TextView
    private lateinit var slogan: TextView

    companion object {
        const val SPLASH_SCREEN = 3500 // Thời gian Splash Screen (ms)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Tạo kênh thông báo
        createNotificationChannel()

        // Kiểm tra và yêu cầu quyền thông báo
        checkAndRequestNotificationPermission()

        // Hiệu ứng chuyển động cho Splash Screen
        setupSplashAnimations()

        // Điều hướng sau Splash
        Handler().postDelayed({
            navigateToDashboard()
        }, SPLASH_SCREEN.toLong())

        // Lên lịch thông báo dựa trên cài đặt (nếu đã cấp quyền)
        scheduleNotifications(this)
    }

    // Tạo hiệu ứng Splash Screen
    private fun setupSplashAnimations() {
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)

        // Kết nối giao diện
        imageView = findViewById(R.id.imageView)
        logo = findViewById(R.id.textView)
        slogan = findViewById(R.id.textView2)

        // Áp dụng hiệu ứng
        imageView.startAnimation(topAnim)
        logo.startAnimation(bottomAnim)
        slogan.startAnimation(bottomAnim)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Điều hướng tới Dashboard
    private fun navigateToDashboard() {
        val intent = Intent(this, DashboardActivity::class.java)

        // Hiệu ứng chuyển tiếp
        val pairs = listOf(
            Pair(imageView, "logo_image"),
            Pair(logo, "logo_text")
        )
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this,
            *pairs.map { androidx.core.util.Pair(it.first, it.second) }.toTypedArray()
        )

        startActivity(intent, options.toBundle())
        finish()
    }

    // Tạo kênh thông báo cho Android 8+
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

    // Kiểm tra và yêu cầu quyền thông báo cho Android 13+ (API 33+)
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

    // Hiển thị thông báo nếu người dùng từ chối quyền
    private fun showPermissionDeniedMessage() {
        Toast.makeText(
            this,
            "Ứng dụng cần quyền thông báo để gửi nhắc nhở hàng ngày.",
            Toast.LENGTH_LONG
        ).show()
    }
}
