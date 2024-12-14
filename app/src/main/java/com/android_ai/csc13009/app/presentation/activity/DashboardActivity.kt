package com.android_ai.csc13009.app.presentation.activity

import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.presentation.fragment.AccountFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class DashboardActivity : AppCompatActivity() {
    private lateinit var bottomNavView : BottomNavigationView
    private lateinit var frame : FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)


        bottomNavView = findViewById(R.id.bottom_nav)
        frame = findViewById(R.id.nav_host_fragment)

        bottomNavView.setOnItemSelectedListener  {
            when (it.itemId) {
                R.id.home -> {
                    // Handle Home
                    true
                }
                R.id.account -> {
                    loadFragment(AccountFragment())
                    true
                }

                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).commit()
    }
}