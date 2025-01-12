package com.android_ai.csc13009.app.presentation.activity

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.presentation.fragment.FragmentWordQuestionTypeChat1
import com.android_ai.csc13009.app.presentation.fragment.WordQuestionFragment
import com.google.android.material.button.MaterialButton

class VocabularyWordActivity : AppCompatActivity() {
    private lateinit var btnCheckAnswer : MaterialButton
    private lateinit var dialog: Dialog
    private lateinit var btnConfirmDialog: Button
    private lateinit var btnClose: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_vocabulary_word)
        loadFragment(FragmentWordQuestionTypeChat1())

        //Hooks
        btnClose = findViewById(R.id.btnClose)

        dialog = Dialog(this)
        dialog.setContentView(R.layout.custom_dialog_answer_correct)
        dialog.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawableResource(R.drawable.custom_dialog_bg)
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.setCancelable(false)

        btnConfirmDialog = dialog.findViewById(R.id.btn_continue)

        btnConfirmDialog.setOnClickListener {
            dialog.dismiss()
        }

        //btnCheckAnswer = findViewById(R.id.btn_check_answer)

//        btnCheckAnswer.setOnClickListener {
//            dialog.show()
//        }

        btnClose.setOnClickListener {
            finish()
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayout, fragment)
            commit()
        }
    }
}