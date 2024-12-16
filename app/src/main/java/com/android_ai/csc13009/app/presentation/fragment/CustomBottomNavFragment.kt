package com.android_ai.csc13009

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class CustomBottomNavFragment : Fragment(R.layout.custom_bottom_nav) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigationView = view.findViewById<BottomNavigationView>(R.id.bottomNavigation)

        // Xử lý sự kiện khi chọn item trong navigation
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    // Xử lý khi chọn Home
                    true
                }
                R.id.nav_dictionary -> {
                    // Xử lý khi chọn Dictionary
                    true
                }
                R.id.nav_exercise -> {
                    // Xử lý khi chọn Exercise
                    true
                }
                R.id.nav_profile -> {
                    // Xử lý khi chọn Profile
                    true
                }
                else -> false
            }
        }
    }

}
