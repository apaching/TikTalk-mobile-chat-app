package com.example.tiktalk.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tiktalk.R
import com.example.tiktalk.databinding.ActivityLoginBinding
import com.example.tiktalk.databinding.ActivityProfileBinding
import com.example.tiktalk.state.AuthenticationStates
import com.example.tiktalk.viewmodel.AuthenticationViewModel

class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding : ActivityProfileBinding
    private lateinit var viewModel : AuthenticationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = AuthenticationViewModel()
        viewModel.getState().observe(this@UserProfileActivity) {
            handleState(it)
        }

        viewModel.getCurrentUserInfo()

    }

    private fun handleState(it : AuthenticationStates) {
        when(it) {
            is AuthenticationStates.Default -> {
                binding.tvUsername.text = it.user?.name
            }

            else -> {}
        }
    }

    companion object {
        fun launch (activity : Activity) {
            activity.startActivity(Intent(activity, UserProfileActivity::class.java))
        }
    }

}