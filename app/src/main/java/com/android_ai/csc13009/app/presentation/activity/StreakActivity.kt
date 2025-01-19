package com.android_ai.csc13009.app.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.android_ai.csc13009.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class StreakActivity : AppCompatActivity() {

    private lateinit var weekLayout: LinearLayout
    private lateinit var streakCountText: TextView
    private lateinit var motivationText: TextView
    private lateinit var continueButton: Button
    private var streakCount: Int = 0 // Biến lưu streakCount
    private var joinDate: Long = 0L // Biến lưu joinDate dưới dạng timestamp

    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_streak)

        // Initialize UI components
        weekLayout = findViewById(R.id.weekLayout)
        streakCountText = findViewById(R.id.streakCountText)
        motivationText = findViewById(R.id.motivationText)
        continueButton = findViewById(R.id.continueButton)

        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Lấy dữ liệu streakCount và joinDate từ Firestore
        fetchUserDataFromFirestore()

        // Handle button click
        continueButton.setOnClickListener {
            //streakCount += 1 // Tăng giá trị streakCount
            //joinDate = System.currentTimeMillis() // Cập nhật joinDate với timestamp hiện tại
             // Cập nhật giao diện
            //saveUserDataToFirestore(streakCount, joinDate) // Lưu giá trị streakCount và joinDate vào Firestore
            goToDashboard() // Chuyển đến DashboardActivity
        }
    }

    private fun fetchUserDataFromFirestore() {
        val userId = firebaseAuth.currentUser?.uid

        if (userId != null) {
            val userRef = firestore.collection("users").document(userId)

            userRef.get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        // Lấy giá trị streakCount và joinDate từ Firestore
                        streakCount = document.getLong("streakCount")?.toInt() ?: 0
                        var joinDateString = document.getString("joinDate") ?: "0"
                        joinDate = joinDateString.toLong()
                        // Kiểm tra logic cập nhật streakCount
                        val currentTime = System.currentTimeMillis()
                        when {
                            isYesterday(joinDate, currentTime) -> streakCount += 1
                            isBeforeYesterday(joinDate, currentTime) -> streakCount = 1
                        }

                        // Cập nhật joinDate thành thời gian hiện tại
                        joinDate = currentTime
                        saveUserDataToFirestore(streakCount, joinDate)

                        // Cập nhật giao diện
                        updateStreakUI(streakCount)
                    } else {
                        println("Document does not exist!")
                    }
                }
                .addOnFailureListener { exception ->
                    println("Error fetching user data: ${exception.message}")
                }
        } else {
            println("User is not authenticated.")
        }
    }

    private fun updateStreakUI(streakCount: Int) {
        // Update streak count
        streakCountText.text = streakCount.toString()

        // Update week layout
        weekLayout.removeAllViews()
        for (i in 1..7) {
            val dayStatus = if (i <= streakCount % 7) R.drawable.ic_check_circle else R.drawable.ic_uncheck_circle
            val dayView = LottieAnimationView(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    100, 100
                ).apply {
                    setMargins(8, 8, 8, 8)
                }
                setImageResource(dayStatus)
            }
            weekLayout.addView(dayView)
        }

        // Update motivation text dynamically
        motivationText.text = when {
            streakCount < 7 -> "Hãy tiếp tục cố gắng để đạt chuỗi 7 ngày hoàn hảo nhé!"
            streakCount % 7 == 0 -> "Chúc mừng! Bạn đã hoàn thành một tuần streak!"
            else -> "Tuyệt vời! Liệu tuần tới bạn có thể đạt một tuần hoàn hảo không nhỉ?"
        }
    }

    private fun saveUserDataToFirestore(newStreakCount: Int, newJoinDate: Long) {
        val userId = firebaseAuth.currentUser?.uid

        if (userId != null) {
            val userRef = firestore.collection("users").document(userId)

            val updates = mapOf(
                "streakCount" to newStreakCount,
                "joinDate" to newJoinDate.toString()
            )

            userRef.update(updates)
                .addOnSuccessListener {
                    println("User data updated successfully.")
                }
                .addOnFailureListener { exception ->
                    println("Error updating user data: ${exception.message}")
                }
        } else {
            println("User is not authenticated.")
        }
    }

    private fun goToDashboard() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun isYesterday(joinDate: Long, currentTime: Long): Boolean {
        val calendar = Calendar.getInstance()

        // Lấy ngày hiện tại
        calendar.timeInMillis = currentTime
        val currentDayOfYear = calendar.get(Calendar.DAY_OF_YEAR)

        // Lấy ngày hôm qua
        calendar.timeInMillis = joinDate
        val joinDayOfYear = calendar.get(Calendar.DAY_OF_YEAR)

        return currentDayOfYear - joinDayOfYear == 1
    }

    private fun isBeforeYesterday(joinDate: Long, currentTime: Long): Boolean {
        val calendar = Calendar.getInstance()

        // Lấy ngày hiện tại
        calendar.timeInMillis = currentTime
        val currentDayOfYear = calendar.get(Calendar.DAY_OF_YEAR)

        // Lấy ngày joinDate
        calendar.timeInMillis = joinDate
        val joinDayOfYear = calendar.get(Calendar.DAY_OF_YEAR)

        return currentDayOfYear - joinDayOfYear > 1
    }
}
