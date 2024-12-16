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
import com.android_ai.csc13009.app.presentation.activity.DashboardActivity
import com.android_ai.csc13009.app.presentation.activity.EditProfileActivity
import kotlin.math.log


class AccountFragment : Fragment() {

    private lateinit var cInfo : CardView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_account, container, false)
        cInfo = view.findViewById(R.id.cInfo)

        cInfo.setOnClickListener {
            val intent = Intent(activity, EditProfileActivity::class.java)
            startActivity(intent)
        }





        return view
    }


}