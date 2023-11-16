package com.example.tiktalk.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.BackgroundColorSpan
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.content.ContextCompat
import com.example.tiktalk.R
import com.example.tiktalk.databinding.ActivityLoginBinding
import com.example.tiktalk.state.AuthenticationStates
import com.example.tiktalk.viewmodel.AuthenticationViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var viewModel : AuthenticationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = AuthenticationViewModel()
        viewModel.getState().observe(this@LoginActivity) {
            handleState(it)
        }


        binding.btnLogin.setOnClickListener {
            viewModel.signIn(binding.etEmail.text.toString(), binding.etPassword.text.toString())
        }

        binding.tvSignUp.apply {
            text = addClickableLink("Don't have an account yet? Sign up", "Sign up") {
                SignupActivity.launch(this@LoginActivity)
            }

            movementMethod = LinkMovementMethod.getInstance()
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.isUserSignedIn()
    }

    private fun handleState (it : AuthenticationStates) {
        when(it) {
            is AuthenticationStates.AlreadySignedIn -> {
                if (it.alreadySignedIn) {
                    HomeActivity.launch(this@LoginActivity)
                    finish()
                }
            }

            is AuthenticationStates.SignedIn -> {
                HomeActivity.launch(this@LoginActivity)
                finish()
            }

            is AuthenticationStates.Error -> {

            }

            else -> {}
        }
    }

    private fun addClickableLink(fullText: String, linkText: String, callback: () -> Unit): SpannableString {
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(p0: View) {
                callback.invoke()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = Color.BLACK

            }
        }

        val startIndex = fullText.indexOf(linkText, 0, true)

        return SpannableString(fullText).apply{
            setSpan(clickableSpan, startIndex, startIndex + linkText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//            setSpan(ForegroundColorSpan(colorYellow), startIndex, startIndex + linkText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//            setSpan(BackgroundColorSpan(colorWhite), startIndex, startIndex + linkText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        }
    }

    companion object {
        fun launch (activity : Activity) {
            activity.startActivity(Intent(activity, LoginActivity::class.java))
        }
    }
}