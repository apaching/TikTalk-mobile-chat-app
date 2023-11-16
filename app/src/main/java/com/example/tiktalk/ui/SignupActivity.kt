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
import com.example.tiktalk.databinding.ActivitySignupBinding
import com.example.tiktalk.state.AuthenticationStates
import com.example.tiktalk.viewmodel.AuthenticationViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class SignupActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySignupBinding
    private lateinit var viewModel : AuthenticationViewModel

    private var auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = AuthenticationViewModel()
        viewModel.getState().observe(this@SignupActivity) {
            renderUi(it)
        }

        binding.tvSignIn.apply {
            text = addClickableLink("Already have an account?", "Already have an account?") {
                finish()
            }

            movementMethod = LinkMovementMethod.getInstance()
        }

        binding.btnSignup.setOnClickListener {
            var isComplete : Boolean = false
            var isVerified : Boolean = false

            if (binding.etName.text.isNullOrEmpty()) {
                binding.tilName.error = "Required field!"
                isComplete = false
            } else {
                binding.tilName.error = null
                isComplete = true
            }

            if (binding.etEmail.text.isNullOrEmpty()) {
                binding.tilEmail.error = "Required field!"
                isComplete = false
            } else {
                binding.tilEmail.error = null
                isComplete = true
            }

            if (binding.etPassword.text.isNullOrEmpty()) {
                binding.tilPassword.error = "Required field!"
                isComplete = false
            } else {
                binding.tilPassword.error = null
                isComplete = true
            }

            if (binding.etReEnterPassword.text.isNullOrEmpty()) {
                binding.tilReEnterPassword.error = "Required field!"
                isComplete = false
            } else {
                binding.tilReEnterPassword.error = null
                isComplete = true
            }

            if (binding.etPassword.text.toString() != binding.etReEnterPassword.text.toString()) {
                binding.tilReEnterPassword.error = "Passwords do not match!"
                isVerified = false
            } else {
                isVerified = true
            }

            if (isComplete && isVerified) {
                val email = binding.etEmail.text.toString()
                val password = binding.etPassword.text.toString()

                viewModel.signUp(email, password)
            }
        }
    }

    private fun renderUi (it : AuthenticationStates) {
        when(it) {
            is AuthenticationStates.SignedUp -> {
                // Add logic for creating user in the database
                val uid = auth.currentUser?.uid.toString()

                viewModel.createUserRecord(
                    uid,
                    binding.etName.text.toString(),
                    binding.etEmail.text.toString()
                )

                auth.signOut()
                finish()
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
            activity.startActivity(Intent(activity, SignupActivity::class.java))
        }
    }
}