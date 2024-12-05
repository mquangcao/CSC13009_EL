package com.android_ai.csc13009.app.presentation.activity

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android_ai.csc13009.R
import com.google.android.material.textfield.TextInputLayout

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
    }
}