package com.example.tiktalk.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import com.example.tiktalk.R
import com.example.tiktalk.databinding.ActivityChatBinding
import com.example.tiktalk.databinding.ToolbarTitleBinding

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val customTitle = TextView(this@ChatActivity)
        customTitle.text = getString(R.string.sample_name_2) //another user name
        customTitle.setTextAppearance(R.style.ActionBarTitleText)


        val layoutParams = ActionBar.LayoutParams(
            ActionBar.LayoutParams.WRAP_CONTENT,
            ActionBar.LayoutParams.WRAP_CONTENT,
            Gravity.CENTER
        )
        supportActionBar?.customView = customTitle
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM

        supportActionBar?.apply {
            customView = customTitle
            displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            setCustomView(customTitle, layoutParams)
        }

            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_24)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                HomeActivity.launch(this@ChatActivity)
            }
        }
        return true
    }


    companion object {
        fun launch(activity : Activity) {
            activity.startActivity(Intent(activity, ChatActivity::class.java))
        }
    }
}