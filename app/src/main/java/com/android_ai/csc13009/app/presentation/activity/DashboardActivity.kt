package com.android_ai.csc13009.app.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.data.remote.repository.FirebaseAuthRepository
import com.android_ai.csc13009.app.data.remote.repository.FirestoreTopicRepository
import com.android_ai.csc13009.app.data.remote.repository.FirestoreUserRepository
import com.android_ai.csc13009.app.data.repository.UserRepository
import com.android_ai.csc13009.app.domain.models.UserModel
import com.android_ai.csc13009.app.presentation.fragment.AccountFragment
import com.android_ai.csc13009.app.presentation.fragment.DictionaryFragment
import com.android_ai.csc13009.app.presentation.fragment.games.GameFragment
import com.android_ai.csc13009.app.presentation.fragment.HomeFragment
import com.android_ai.csc13009.app.presentation.fragment.LearnFragment
import com.android_ai.csc13009.app.presentation.service.SyncDataFromFirestore
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DashboardActivity : AppCompatActivity() {
    private lateinit var bottomNavView : BottomNavigationView
    private lateinit var frame : FrameLayout
    private lateinit var userRepository: UserRepository

    private lateinit var currentUser : UserModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)
        userRepository = UserRepository(FirebaseAuthRepository(FirebaseAuth.getInstance()),
            FirestoreUserRepository(FirebaseFirestore.getInstance()))
        GlobalScope.launch(Dispatchers.Main) {
            currentUser = withContext(Dispatchers.IO) {
                userRepository.getCurrentUser()!!
            }

            val currentTime = System.currentTimeMillis()
            val joinDateString = currentUser.joinDate // Giả sử joinDate là Long (timestamp)
            val joinDate: Long = joinDateString.toLong()

            // Kiểm tra nếu joinDate là hôm qua
            if (isYesterday(joinDate, currentTime)) {
                // Chuyển đến StreakActivity
                val intent = Intent(this@DashboardActivity, StreakActivity::class.java)
                startActivity(intent)
                finish()
            }
        }


        bottomNavView = findViewById(R.id.bottom_nav)
        frame = findViewById(R.id.nav_host_fragment)

        // Mặc định load HomeFragment khi vào DashboardActivity
        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }

        bottomNavView.setOnItemSelectedListener  {
            when (it.itemId) {
                R.id.home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.learn -> {
                    loadFragment(LearnFragment())
                    true
                }
                R.id.game -> {
                    loadFragment(GameFragment())
                    true
                }
                R.id.dictionary -> {
                    loadFragment(DictionaryFragment())
                    true
                }
                R.id.account -> {
                    loadFragment(AccountFragment())
                    true
                }
                else -> false
            }
        }
    }



    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).commit()
    }

    fun changeActivity(context: Context, targetActivity: Class<out AppCompatActivity>) {
        val intent = Intent(context, targetActivity)
        context.startActivity(intent)
    }

    fun changeActivity(
        context: Context,
        targetActivity: Class<out AppCompatActivity>,
        passedData: Serializable)
    {
        val intent = Intent(context, targetActivity)
        intent.putExtra("passedData", passedData)
        context.startActivity(intent)
    }

    fun changeActivity(
        context: Context,
        targetActivity: Class<out AppCompatActivity>,
        passedDataList: ArrayList<Serializable>
    ) {
        val intent = Intent(context, targetActivity)

        // Adding the list to intent as a single key
        intent.putExtra("passedDataList", passedDataList)
        context.startActivity(intent)
    }

    private fun isYesterday(date1: Long, date2: Long): Boolean {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        // Chuyển đổi timestamp thành ngày
        val date1Str = sdf.format(Date(date1))
        val date2Str = sdf.format(Date(date2))

        // Chuyển đổi ngày hiện tại thành hôm qua
        val calendar = Calendar.getInstance()
        calendar.time = sdf.parse(date2Str)!!
        calendar.add(Calendar.DAY_OF_YEAR, -1)
       // val yesterdayStr = sdf.format(calendar.time)

        // So sánh date1 với ngày hôm qua
        return date1Str != date2Str
    }
}