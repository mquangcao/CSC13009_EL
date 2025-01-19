package com.android_ai.csc13009.app.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.data.local.AppDatabase
import com.android_ai.csc13009.app.data.local.entity.UserEntity
import com.android_ai.csc13009.app.data.remote.model.LoginState
import com.android_ai.csc13009.app.data.remote.repository.FirebaseAuthRepository
import com.android_ai.csc13009.app.data.remote.repository.FirestoreTopicRepository
import com.android_ai.csc13009.app.data.remote.repository.FirestoreUserRepository
import com.android_ai.csc13009.app.data.repository.UserRepository
import com.android_ai.csc13009.app.presentation.viewmodel.UserViewModel
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var email : TextInputLayout
    private lateinit var password : TextInputLayout
    private lateinit var btnCallSingUp : Button
    private lateinit var  tvLogoName : TextView
    private lateinit var  tvLogoDesc : TextView
    private lateinit var  ivLogo : ImageView
    private lateinit var btnForgotPassword : Button
    private lateinit var btnSingIn : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        btnCallSingUp = findViewById(R.id.btnCallSingUp)
        tvLogoName = findViewById(R.id.tvLogoName)
        tvLogoDesc = findViewById(R.id.tvLogoDesc)
        ivLogo = findViewById(R.id.ivLogo)
        btnForgotPassword = findViewById(R.id.btnForgotPassword)
        btnSingIn = findViewById(R.id.btnSingIn)


        btnCallSingUp.setOnClickListener {
            val intent = Intent(this, SingUpActivity::class.java)
            val pairs = listOf(
                Pair(ivLogo, "logo_image"),
                Pair(btnForgotPassword, "password_confirm"),
                Pair(btnSingIn, "go_btn"),
            )
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, *pairs.map { androidx.core.util.Pair(it.first, it.second) }.toTypedArray())

            startActivity(intent, options.toBundle())


        }

        btnSingIn.setOnClickListener {
            val email = email.editText?.text.toString()
            val password = password.editText?.text.toString()


            // Initialize repositories with Firebase instances
            val firebaseAuth = FirebaseAuth.getInstance()
            val firestore = FirebaseFirestore.getInstance()

            val userRepository = UserRepository(FirebaseAuthRepository(firebaseAuth), FirestoreUserRepository(firestore))
            val viewModel = UserViewModel(userRepository)
            // Call the register method in the ViewModel (which interacts with Repository)
            viewModel.loginUser(email, password)

            viewModel.loginState.observe(this) { state ->
                when (state) {
                    is LoginState.Success -> {
                        onLoginSuccess()
                    }
                    is LoginState.Error -> {
                        onLoginFailure(state.message)
                    }
                    LoginState.Loading -> {
                        // Show a loading indicator
                    }
                }
            }
        }
    }

    private fun onLoginSuccess() {
        // Logic to handle successful login (e.g., navigate to another screen)
        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
        // Optionally, navigate to the login or main screen
        lifecycleScope.launch {
            val user = UserRepository(FirebaseAuthRepository(FirebaseAuth.getInstance()), FirestoreUserRepository(FirebaseFirestore.getInstance())).getCurrentUser()

            if (user != null) {

                val currentUser = UserEntity(
                    id = user.id,
                    firstName = user.fullName,
                    lastName = user.fullName,
                    joinDate = user.joinDate,
                    avatar = user.avatar,
                    streakCount = user.streakCount,
                    level = user.level
                )
                AppDatabase.getInstance(this@LoginActivity).userDao().insertUser(currentUser)
                if(user.level.isEmpty()){
                    val intent = Intent(this@LoginActivity, ChooseLevelActivity::class.java)
                    intent.putExtra("userId", user.id)
                    startActivity(intent)
                    return@launch
                }
                val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                startActivity(intent)

            }
        }

    }

    private fun onLoginFailure(errorMessage: String) {
        // Logic to handle login failure
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }
}