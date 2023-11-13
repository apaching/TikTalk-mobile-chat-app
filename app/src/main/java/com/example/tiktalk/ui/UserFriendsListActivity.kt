package com.example.tiktalk.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import com.example.tiktalk.R
import com.example.tiktalk.databinding.ActivityUserFriendsListBinding

class UserFriendsListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserFriendsListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserFriendsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val customTitle = TextView(this@UserFriendsListActivity)
        customTitle.text = getString(R.string.friends_list)
        customTitle.setTextAppearance(R.style.ActionBarTitleText)

        supportActionBar?.customView = customTitle
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_24)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                HomeActivity.launch(this@UserFriendsListActivity)
            }
        }
        return true
    }

    companion object {
        fun launch (activity : Activity) {
            activity.startActivity(Intent(activity, UserFriendsListActivity::class.java))
        }
    }
}