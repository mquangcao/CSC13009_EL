package com.android_ai.csc13009.app.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android_ai.csc13009.HomeActivity
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.presentation.viewmodel.UserViewModel
import com.android_ai.csc13009.app.presentation.viewmodel.UserViewModelFactory
import com.android_ai.csc13009.app.data.local.AppDatabase
import com.android_ai.csc13009.app.data.local.dao.UserDao
import com.android_ai.csc13009.app.data.repository.UserRepository

class WordLevelSelection : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_level_division)

        // Lấy UserDao từ Room Database
        //val userDao = AppDatabase.getInstance(this).userDao()

        // Lấy TextView bằng findViewById
        val tvUserName = findViewById<TextView>(R.id.tvUserName)

        // Khởi tạo UserRepository và UserViewModel
        //val userDao: UserDao = AppDatabase.getInstance(this).userDao()
        //val userRepository = UserRepository(userDao)
        //val userViewModelFactory = UserViewModelFactory(userRepository)
        //userViewModel = userViewModelFactory.create(UserViewModel::class.java)

        // ID của người dùng cần hiển thị
        val userId = "2" // Thay ID của người dùng

        userViewModel.getUserById(userId) { userModel ->
            if (userModel != null) {
                tvUserName.text = userModel.fullName // Cập nhật tên đầy đủ
            } else {
                tvUserName.text = "User not found" // Không tìm thấy người dùng
            }
        }
        // Dữ liệu mẫu
        /*val sampleUsers = listOf(
            UserEntity(
                id = "1",
                firstName = "John",
                lastName = "Doe",
                joinDate = "2023-12-01",
                avatar = "https://example.com/avatar1.png",
                streakCount = 5,
                level = "Basic"
            ),
            UserEntity(
                id = "2",
                firstName = "Jane",
                lastName = "Smith",
                joinDate = "2023-12-05",
                avatar = "https://example.com/avatar2.png",
                streakCount = 10,
                level = "Proficient"
            ),
            UserEntity(
                id = "3",
                firstName = "Alice",
                lastName = "Brown",
                joinDate = "2023-11-25",
                avatar = "https://example.com/avatar3.png",
                streakCount = 15,
                level = "Independent"
            )
        )

        // Chèn dữ liệu mẫu
        lifecycleScope.launch {
            userDao.insertUsers(sampleUsers)
        }*/

        findViewById<TextView>(R.id.btnBasic).setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent) // Chuyển sang màn hình Basic
        }

        // Gắn sự kiện cho nút Independent
        findViewById<TextView>(R.id.btnIndependent).setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent) // Chuyển sang màn hình Independent
        }

        // Gắn sự kiện cho nút Proficient
        findViewById<TextView>(R.id.btnProficient).setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent) // Chuyển sang màn hình Proficient
        }


        // Xử lý khi người dùng chọn các mục trong BottomNavigationView
        /*bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    // Mở HomeActivity
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.putExtra("selectedItem", R.id.nav_home)
                    startActivity(intent)
                    true
                }
                R.id.nav_dictionary -> {
                    // Mở DictionaryActivity
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_exercise -> {
                    // Mở ExerciseActivity
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_profile -> {
                    // Mở ProfileActivity
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }*/
    }
}