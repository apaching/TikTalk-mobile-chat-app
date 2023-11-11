package com.example.tiktalk.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tiktalk.R
import com.example.tiktalk.databinding.ActivityLoginBinding
import com.example.tiktalk.databinding.ActivityProfileBinding

class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding : ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    companion object {
        fun launch (activity : Activity) {
            activity.startActivity(Intent(activity, UserProfileActivity::class.java))
        }
    }

}