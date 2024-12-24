package com.android_ai.csc13009.app.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android_ai.csc13009.R

class LoginActivity : AppCompatActivity() {
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
            val dashboardIntent = Intent(this, DashboardActivity::class.java)
            startActivity(dashboardIntent)
        }
    }
}