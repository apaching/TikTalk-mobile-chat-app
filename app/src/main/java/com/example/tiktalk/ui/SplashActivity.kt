package com.example.tiktalk.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tiktalk.R
import com.example.tiktalk.databinding.ActivitySplashBinding
import com.example.tiktalk.state.AuthenticationStates
import com.example.tiktalk.viewmodel.AuthenticationViewModel
import java.util.Timer
import kotlin.concurrent.schedule

class SplashActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySplashBinding
    private lateinit var viewModel : AuthenticationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = AuthenticationViewModel()
        viewModel.getState().observe(this@SplashActivity) {
            handleState(it)
        }

        Timer().schedule(3000) {
            viewModel.isUserSignedIn()
        }
    }

    private fun handleState(it : AuthenticationStates) {
        when(it) {
            is AuthenticationStates.AlreadySignedIn -> {
                if (it.alreadySignedIn) {
                    HomeActivity.launch(this@SplashActivity)
                    finish()
                } else {
                    // Launch LoginActivity
                }
            }

            else -> {}
        }
    }
}