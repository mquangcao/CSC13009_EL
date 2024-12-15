package com.android_ai.csc13009.app.presentation.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android_ai.csc13009.R
import com.google.android.material.button.MaterialButton

class EditProfileActivity : AppCompatActivity() {
    private lateinit var btnArrowBack : ImageButton
    private lateinit var dialog : Dialog
    private lateinit var btnConfirm : Button
    private lateinit var btnCancel : MaterialButton
    private lateinit var btnLogout : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_profile)

        btnArrowBack = findViewById(R.id.btnArrowBack)
        btnArrowBack.setOnClickListener {
            finish()
        }

        dialog = Dialog(this)
        dialog.setContentView(R.layout.custom_dialog_confirm_logout)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawableResource(R.drawable.custom_dialog_bg)
        dialog.setCancelable(false)

        btnConfirm = dialog.findViewById(R.id.btnConfirm)
        btnCancel = dialog.findViewById(R.id.btnCancel)

        btnConfirm.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            this.finish()
            dialog.dismiss()
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnLogout = findViewById(R.id.btnLogout)

        btnLogout.setOnClickListener {
            dialog.show()
        }

    }
}