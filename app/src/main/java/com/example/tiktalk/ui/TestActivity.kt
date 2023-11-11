package com.example.tiktalk.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tiktalk.databinding.ActivityTestBinding
import com.example.tiktalk.viewmodel.FriendsViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class TestActivity : AppCompatActivity() {

    private lateinit var binding : ActivityTestBinding
    private lateinit var viewModel : FriendsViewModel
    private var auth = Firebase.auth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = FriendsViewModel()

        binding.btn.setOnClickListener {
            viewModel.sendFriendRequest(auth.currentUser?.uid, "BEmGaNYKszLzD3Dx2woRKIDQVJ22")
        }

        binding.btn2.setOnClickListener {
            auth.signOut()
            finish()
        }
    }

    companion object {
        fun launch (activity : Activity) {
            activity.startActivity(Intent(activity, TestActivity::class.java))
        }
    }
}