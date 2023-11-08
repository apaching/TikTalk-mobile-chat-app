package com.example.tiktalk.ui

import android.app.Activity
import android.content.Intent
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

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvSignUp.apply {
            text = addClickableLink("Don't have an account yet? Sign up", "Sign up") {
                SignupActivity.launch(this@LoginActivity)
            }

            movementMethod = LinkMovementMethod.getInstance()
        }
    }

    private fun addClickableLink(fullText: String, linkText: String, callback: () -> Unit): SpannableString {
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(p0: View) {
                callback.invoke()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
                ds.typeface = Typeface.DEFAULT_BOLD
            }
        }

        val startIndex = fullText.indexOf(linkText, 0, true)

        val colorYellow = ContextCompat.getColor(this@LoginActivity, R.color.white)
        val colorWhite = ContextCompat.getColor(this@LoginActivity, R.color.white)

        return SpannableString(fullText).apply{
            setSpan(clickableSpan, startIndex, startIndex + linkText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            setSpan(ForegroundColorSpan(colorYellow), startIndex, startIndex + linkText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            setSpan(BackgroundColorSpan(colorWhite), startIndex, startIndex + linkText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        }
    }

    companion object {
        fun launch (activity : Activity) {
            activity.startActivity(Intent(activity, LoginActivity::class.java))
        }
    }
}