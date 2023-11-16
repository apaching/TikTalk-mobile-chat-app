package com.example.tiktalk.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.tiktalk.R
import com.example.tiktalk.databinding.ActivityChangePasswordBinding
import com.example.tiktalk.state.AuthenticationStates
import com.example.tiktalk.viewmodel.AuthenticationViewModel

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePasswordBinding
    private lateinit var viewModel: AuthenticationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)



        viewModel = AuthenticationViewModel()
        viewModel.getState().observe(this@ChangePasswordActivity){
            handleState(it)
        }

        with(binding){
            btnChangePassword.setOnClickListener {
                 val emailAddress = etEmail.text.toString()

                viewModel.changeUserPassword(emailAddress)
            }
            btnCancel.setOnClickListener {
                finish()
            }
        }

    }

    private fun handleState(it  :AuthenticationStates){
        when(it){
            is AuthenticationStates.ResetPasswordEmailSent -> {
                setContentView(R.layout.change_password_message_screen)
                val button = findViewById<Button>(R.id.btn_changePassword)
                button.setOnClickListener {
                    finish()

                }
            }

            is AuthenticationStates.Error -> {

            }

            else ->{}
        }
    }
    companion object {
        fun launch(activity : Activity) {
            activity.startActivity(Intent(activity, ChangePasswordActivity::class.java))
        }
    }
}