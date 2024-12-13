/*
package com.android_ai.csc13009

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android_ai.csc13009.app.presentation.activity.LoginActivity

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
   */
/* lateinit var topAnim : Animation
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
            val intent = Intent(this, LoginActivity::class.java)

            val pairs = listOf(
                Pair(imageView, "logo_image"),
                Pair(logo, "logo_text"),
            )

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, *pairs.map { androidx.core.util.Pair(it.first, it.second) }.toTypedArray())

            startActivity(intent, options.toBundle())
            finish()
        }, SPLASH_SCREEN.toLong())
    }*//*


}*/

package com.android_ai.csc13009
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.android_ai.csc13009.app.data.local.AppDatabase
import com.android_ai.csc13009.app.data.local.entity.UserEntity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_level_division)

        // Lấy UserDao từ Room Database
        val userDao = AppDatabase.getInstance(this).userDao()

        // Lấy TextView bằng findViewById
        val tvUserName = findViewById<TextView>(R.id.tvUserName)

        // ID của người dùng cần hiển thị
        val userId = "1" // Thay ID của người dùng

        lifecycleScope.launch {
            val user = userDao.getUserById(userId)
            if (user != null) {
                val fullName = "${user.firstName} ${user.lastName}" // Gộp họ tên
                tvUserName.text = fullName // Cập nhật TextView
            } else {
                tvUserName.text = "User not found" // Nếu không tìm thấy
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
            val intent = Intent(this, BasicActivity::class.java)
            startActivity(intent) // Chuyển sang màn hình Basic
        }

        // Gắn sự kiện cho nút Independent
        findViewById<TextView>(R.id.btnIndependent).setOnClickListener {
            val intent = Intent(this, BasicActivity::class.java)
            startActivity(intent) // Chuyển sang màn hình Independent
        }

        // Gắn sự kiện cho nút Proficient
        findViewById<TextView>(R.id.btnProficient).setOnClickListener {
            val intent = Intent(this, BasicActivity::class.java)
            startActivity(intent) // Chuyển sang màn hình Proficient
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.customBottomNav)

        // Xử lý khi người dùng chọn các mục trong BottomNavigationView
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
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
        }
    }
}




