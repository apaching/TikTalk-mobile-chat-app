package com.example.tiktalk.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tiktalk.R
import com.example.tiktalk.databinding.ActivityChangePasswordBinding

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}