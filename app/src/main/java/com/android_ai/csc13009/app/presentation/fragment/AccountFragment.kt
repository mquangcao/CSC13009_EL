package com.android_ai.csc13009.app.presentation.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.data.remote.repository.FirebaseAuthRepository
import com.android_ai.csc13009.app.data.remote.repository.FirestoreUserRepository
import com.android_ai.csc13009.app.data.repository.UserRepository
import com.android_ai.csc13009.app.presentation.activity.DashboardActivity
import com.android_ai.csc13009.app.presentation.activity.EditProfileActivity
import com.android_ai.csc13009.app.presentation.activity.LoginActivity
import com.android_ai.csc13009.app.presentation.activity.WordLevelSelection
import com.android_ai.csc13009.app.presentation.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.log


class AccountFragment : Fragment() {
    private lateinit var cLevel : CardView
    private lateinit var cInfo : CardView
    private lateinit var cLogout : CardView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_account, container, false)
        cInfo = view.findViewById(R.id.cInfo)

        cInfo.setOnClickListener {
            val intent = Intent(activity, EditProfileActivity::class.java)
            startActivity(intent)
        }

        cLevel = view.findViewById(R.id.cLevel)
        cLevel.setOnClickListener{
            val intent = Intent(activity, WordLevelSelection::class.java)
            startActivity(intent)
        }

        cLogout = view.findViewById(R.id.cLogout)
        cLogout.setOnClickListener{
            // Initialize repositories with Firebase instances
            val firebaseAuth = FirebaseAuth.getInstance()
            val firestore = FirebaseFirestore.getInstance()

            val userRepository = UserRepository(FirebaseAuthRepository(firebaseAuth), FirestoreUserRepository(firestore))
            val viewModel = UserViewModel(userRepository)
            // Call the register method in the ViewModel (which interacts with Repository)
            viewModel.logout()
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }

        return view
    }


}