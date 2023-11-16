package com.example.tiktalk.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import com.example.tiktalk.R
import com.example.tiktalk.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val customTitle = TextView(this@SettingsActivity)
        customTitle.text = getString(R.string.settings)
        customTitle.setTextAppearance(R.style.ActionBarTitleText)

        supportActionBar?.customView = customTitle
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_24)

        binding.btnChangePassword.setOnClickListener {
            ChangePasswordActivity.launch(this@SettingsActivity)
            finish()
        }

    }

    companion object {
        fun launch(activity : Activity) {
            activity.startActivity(Intent(activity, SettingsActivity::class.java))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return true
    }
}