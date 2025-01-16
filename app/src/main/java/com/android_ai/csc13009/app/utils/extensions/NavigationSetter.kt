package com.android_ai.csc13009.app.utils.extensions;


import android.app.AlertDialog
import android.view.View;
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.android_ai.csc13009.R

public class NavigationSetter {
    companion object {
        fun setBackButton(widget: View, activity: FragmentActivity) {
            widget.setOnClickListener {
                activity.onBackPressedDispatcher.onBackPressed()
            }
        }

//        fun setBackButtonConfirmation(widget: View, activity: AppCompatActivity, title: String = "Are you sure?", message: String = "Do you really want to go back?") {
//            setActivityBackConfirmation(activity, title, message)
//            setBackButton(widget, activity)
//
//        }

        fun setActivityBackConfirmation(
            activity: AppCompatActivity,
            title: String = activity.getString(R.string.back_prompt_title),
            message: String = activity.getString(R.string.back_prompt_description)
        ) {
            activity.onBackPressedDispatcher.addCallback(activity, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    showBackConfirmationDialog(activity, title, message)
                }
            })
        }

        private fun showBackConfirmationDialog(activity: AppCompatActivity, title: String, message: String) {
            AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Yes") { _, _ ->
                    activity.finish()  // Go back
                }
                .setNegativeButton("No", null) // Cancel action
                .show()
        }
    }
}
