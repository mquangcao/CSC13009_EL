package com.android_ai.csc13009.app.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.data.remote.model.LoginState
import com.android_ai.csc13009.app.data.remote.repository.FirebaseAuthRepository
import com.android_ai.csc13009.app.data.remote.repository.FirestoreUserRepository
import com.android_ai.csc13009.app.data.repository.UserRepository
import com.android_ai.csc13009.app.presentation.viewmodel.UserViewModel
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User

class SingUpActivity : AppCompatActivity() {
    private lateinit var btnCallSingUp : Button
    private lateinit var email : TextInputLayout
    private lateinit var singup_password : TextInputLayout
    private lateinit var singup_password_confirm : TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sing_up)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnCallSingUp = findViewById(R.id.btnCallSingUp)
        email = findViewById(R.id.email)
        singup_password = findViewById(R.id.singup_password)
        singup_password_confirm = findViewById(R.id.singup_password_confirm)

        btnCallSingUp.setOnClickListener {
            singUpAccount()
        }

    }

    private fun singUpAccount() {
        val email = email.editText?.text.toString()
        val password = singup_password.editText?.text.toString()
        val passwordConfirm = singup_password_confirm.editText?.text.toString()

        if (email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
            this.email.error = "Please enter email"
            singup_password.error = "Please enter password"
            singup_password_confirm.error = "Please enter password confirm"
            return
        }

        if (password.length < 6) {
            singup_password.error = "Password must be at least 6 characters long" //Due to FirebaseAuth
            return
        }

        if (password != passwordConfirm) {
            this.singup_password_confirm.error = "Confirm Password not match"
            return
        }

        // Initialize repositories with Firebase instances
        val firebaseAuth = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance()

        val userRepository = UserRepository(FirebaseAuthRepository(firebaseAuth), FirestoreUserRepository(firestore))
        val viewModel = UserViewModel(userRepository)
        // Call the register method in the ViewModel (which interacts with Repository)
        viewModel.registerUser(email, password, "", "")

        viewModel.loginState.observe(this, { state ->
            when (state) {
                is LoginState.Success -> {
                    onRegisterSuccess()
                }
                is LoginState.Error -> {
                    // Show error message
                    onRegisterFailure(state.message)
                }
                LoginState.Loading -> {
                    // Show a loading indicator
                }
            }
        })
    }

    private fun onRegisterSuccess() {
        // Logic to handle successful registration (e.g., navigate to another screen)
        Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
        // Optionally, navigate to the login or main screen
        startActivity(Intent(this, LoginActivity::class.java))
    }

    private fun onRegisterFailure(errorMessage: String) {
        // Logic to handle registration failure
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }
}