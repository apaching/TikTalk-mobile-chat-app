package com.example.tiktalk.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.example.tiktalk.R
import com.example.tiktalk.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {

    private lateinit var chatBinding: ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        chatBinding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(chatBinding.root)

        val toolbar : Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            title = "John Doe"
        }
    }

    companion object {
        fun launch(activity : Activity) {
            activity.startActivity(Intent(activity, ChatActivity::class.java))
        }
    }
}