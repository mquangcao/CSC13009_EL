package com.android_ai.csc13009.app.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.data.remote.repository.FirestoreTopicRepository
import com.android_ai.csc13009.app.presentation.fragment.AccountFragment
import com.android_ai.csc13009.app.presentation.fragment.DictionaryFragment
import com.android_ai.csc13009.app.presentation.fragment.games.GameFragment
import com.android_ai.csc13009.app.presentation.fragment.HomeFragment
import com.android_ai.csc13009.app.presentation.fragment.LearnFragment
import com.android_ai.csc13009.app.presentation.service.SyncDataFromFirestore
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.io.Serializable

class DashboardActivity : AppCompatActivity() {
    private lateinit var bottomNavView : BottomNavigationView
    private lateinit var frame : FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)


        bottomNavView = findViewById(R.id.bottom_nav)
        frame = findViewById(R.id.nav_host_fragment)

        // Mặc định load HomeFragment khi vào DashboardActivity
        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }
        lifecycleScope.launch {
            Log.e("FirestoreTopicRepository", "Checkkkkk: ")
            test()
        }

        bottomNavView.setOnItemSelectedListener  {
            when (it.itemId) {
                R.id.home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.learn -> {
                    loadFragment(LearnFragment())
                    true
                }
                R.id.game -> {
                    loadFragment(GameFragment())
                    true
                }
                R.id.dictionary -> {
                    loadFragment(DictionaryFragment())
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

    private suspend fun test() {
        val firestore = FirebaseFirestore.getInstance()
        val sync = SyncDataFromFirestore("beginner", firestore, this)
        sync.run()
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).commit()
    }

    fun changeActivity(context: Context, targetActivity: Class<out AppCompatActivity>) {
        val intent = Intent(context, targetActivity)
        context.startActivity(intent)
    }

    fun changeActivity(
        context: Context,
        targetActivity: Class<out AppCompatActivity>,
        passedData: Serializable)
    {
        val intent = Intent(context, targetActivity)
        intent.putExtra("passedData", passedData)
        context.startActivity(intent)
    }

    fun changeActivity(
        context: Context,
        targetActivity: Class<out AppCompatActivity>,
        passedDataList: ArrayList<Serializable>
    ) {
        val intent = Intent(context, targetActivity)

        // Adding the list to intent as a single key
        intent.putExtra("passedDataList", passedDataList)
        context.startActivity(intent)
    }
}